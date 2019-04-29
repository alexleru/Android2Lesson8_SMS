package ru.alexey.android2lesson8_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

public class TakeCurrentTime extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent != null && intent.getAction() != null) {
            Calendar calendar = Calendar.getInstance();
            String time = "" + calendar.getTime();
            Toast.makeText(context, time, Toast.LENGTH_LONG).show();
        }
    }
}
