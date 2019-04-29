package ru.alexey.android2lesson8_sms;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private TextView textViewReceiveSms;
    private EditText editTextSendSMSNumber;
    private EditText editTextSendSMSBody;
    private Button buttonSend;
    private Boolean statusPermission = false;
    private TakeCurrentTime receiverTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //для обновления поля (которое получает СМС) сделал передал его через Singleton
        Singleton.getInstance().findby(textViewReceiveSms);
        //проверяю разом все разрешения
        requestPermissionFor();
        //регистрирую ресивер для времени
        receiverTimeRegister();
    }

    private void receiverTimeRegister() {
        receiverTime = new TakeCurrentTime();
        registerReceiver(receiverTime, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    private void initView() {
        textViewReceiveSms = findViewById(R.id.receive_text);
        editTextSendSMSNumber = findViewById(R.id.send_number);
        editTextSendSMSBody = findViewById(R.id.send_text);
        buttonSend = findViewById(R.id.send_button);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionFor();
                if (statusPermission){
                    smsSendMessage();
                }
            }
        });
    }

    //Запрашиваем разрешения если они отсутствуют!
    private void requestPermissionFor () {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && isPermissionName(Manifest.permission.SEND_SMS)
                && isPermissionName(Manifest.permission.READ_PHONE_STATE)
                && isPermissionName(Manifest.permission.RECEIVE_SMS)) {
                 ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.RECEIVE_SMS},
                                MY_PERMISSIONS_REQUEST);
        } else {
            statusPermission = true;
        }
    }

    private boolean isPermissionName(String permissionName){
        return ContextCompat.checkSelfPermission(this,
                permissionName) != PackageManager.PERMISSION_GRANTED;
    }

    //Получаем ответ на разрешение и в зависимости от ответа действуем
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "Now you can send and receive SMS",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You can't send and receive SMS. Check permissions",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //Отправка сообщения
    public void smsSendMessage() {
        String number = editTextSendSMSNumber.getText().toString();
        String smsMessage = editTextSendSMSBody.getText().toString();
        if((!number.equals(""))
                && (!smsMessage.equals(""))) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage
                    (number, null, smsMessage,
                            null, null);
            Toast.makeText(getApplicationContext(),
                    "Message was sent",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "You should fill in number and telephone",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverTime);
    }
}
