package ru.alexey.android2lesson8_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class ReceiverSMS extends BroadcastReceiver {
    private static final String PDUS = "pdus";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getAction() != null){
            Object[] pdus  = (Object[])intent.getExtras().get(PDUS);
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            String smsFromPhone = messages[0].getDisplayOriginatingAddress();
            StringBuilder body = new StringBuilder();
            for (SmsMessage message : messages) {
                body.append(message.getMessageBody());
            }
            String bodyText = body.toString();

            Singleton.getInstance().setText(smsFromPhone + "\n" + bodyText);
        }
    }
}
