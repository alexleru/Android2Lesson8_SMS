package ru.alexey.android2lesson8_sms;

import android.widget.TextView;

public class Singleton {
    public static Singleton instance;
    private static TextView textView;
    private Singleton(){
    }
    public static Singleton getInstance(){
        if(instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void findby(TextView textView){
        this.textView = textView;
    }

    public void setText(String sms){
        textView.setText(sms);
    }
}
