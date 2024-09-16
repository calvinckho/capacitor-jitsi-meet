package com.capacitor.jitsi.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

public class JitsiBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "JitsiBroadcastReceiver";
    private Jitsi jitsi;

    public void setModule(Jitsi module) {
        this.jitsi = module;
    }

    public void onReceive(Context context, Intent intent) {
        String actionName = (String) intent.getSerializableExtra("eventName");
        String data = (String) intent.getSerializableExtra("data");
        // Timber.tag(TAG).d("JitsiMeetView: " + actionName + ", " + data);
        if (jitsi != null) {
            jitsi.onEventReceived(actionName, data);
        }
    }
}
