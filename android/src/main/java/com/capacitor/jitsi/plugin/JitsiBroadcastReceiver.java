package com.capacitor.jitsi.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class JitsiBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "JitsiBroadcastReceiver";
    private Jitsi jitsi;

    public void setModule(Jitsi module) {
        this.jitsi = module;
    }

    public void onReceive(Context context, Intent intent) {
        String eventName = (String) intent.getSerializableExtra("eventName");
        if (jitsi != null) {
            jitsi.onEventReceived(eventName);
        }
    }
}