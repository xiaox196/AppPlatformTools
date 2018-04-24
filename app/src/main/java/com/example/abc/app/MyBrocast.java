package com.example.abc.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by D444Q52 on 2015/10/10.
 */
public class MyBrocast extends BroadcastReceiver {
    static final String action_boot="android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(action_boot))
        {
            Intent myintent=new Intent();
            myintent.setClass(context,MainActivity.class);
            myintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myintent);
        }
    }
}
