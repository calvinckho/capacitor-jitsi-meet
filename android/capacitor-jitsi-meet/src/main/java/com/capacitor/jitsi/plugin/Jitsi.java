package com.capacitor.jitsi.plugin;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import android.content.IntentFilter;
import android.util.Log;
import android.content.Intent;
import android.Manifest;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jitsi.meet.sdk.BroadcastIntentHelper;

@NativePlugin(
    permissions={
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
      }
  )
public class Jitsi extends Plugin {
    private static final String TAG = "CapacitorJitsiMeet";

    @PluginMethod()
    public void joinConference(PluginCall call) {
        String url = call.getString("url");
        String roomName = call.getString("roomName");
        String token = call.getString("token");
        String displayName = call.getString("displayName");
        String email = call.getString("email");
        String avatarURL = call.getString("avatarURL");
        Boolean startWithAudioMuted = call.getBoolean("startWithAudioMuted");
        Boolean startWithVideoMuted = call.getBoolean("startWithVideoMuted");
        Boolean chatEnabled = call.getBoolean("chatEnabled");
        Boolean inviteEnabled = call.getBoolean("inviteEnabled");
        Boolean callIntegrationEnabled = call.getBoolean("callIntegrationEnabled");

        JitsiBroadcastReceiver receiver = new JitsiBroadcastReceiver();
        receiver.setModule(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("onConferenceWillJoin");
        filter.addAction("onConferenceJoined");
        filter.addAction("onConferenceLeft"); // intentionally uses the obsolete onConferenceLeft in order to be consistent with iOS deployment and broadcast to JS listeners
        getContext().registerReceiver(receiver, filter);

        if(url == null) {
            call.reject("Must provide an url");
            return;
        }
        if(roomName == null) {
            call.reject("Must provide an conference room name");
            return;
        }
        if(startWithAudioMuted == null) {
            startWithAudioMuted = false;
        }
        if(startWithVideoMuted == null) {
            startWithVideoMuted = false;
        }
        if(chatEnabled == null) {
            chatEnabled = true;
        }
        if(inviteEnabled == null) {
            chatEnabled = true;
        }
        if(callIntegrationEnabled == null) {
            callIntegrationEnabled = true;
        }
        Log.v(TAG, "display url: " + url);

        Intent intent = new Intent(getActivity(), JitsiActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("token", token);
        intent.putExtra("roomName", roomName);
        intent.putExtra("displayName", displayName);
        intent.putExtra("email", email);
        intent.putExtra("avatarURL", avatarURL);
        intent.putExtra("startWithAudioMuted", startWithAudioMuted);
        intent.putExtra("startWithVideoMuted", startWithVideoMuted);
        intent.putExtra("chatEnabled", chatEnabled);
        intent.putExtra("inviteEnabled", inviteEnabled);
        intent.putExtra("callIntegrationEnabled", callIntegrationEnabled);

        getActivity().startActivity(intent);

        JSObject ret = new JSObject();
        ret.put("success", true);
        call.resolve(ret);
    }

    @PluginMethod()
    public void leaveConference(PluginCall call) {
        Log.v(TAG, "leaving conference");
        Intent leaveBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(leaveBroadcastIntent);

        JSObject ret = new JSObject();
        ret.put("success", true);
        call.resolve(ret);
    }

    public void onEventReceived(String eventName) {
        bridge.triggerWindowJSEvent(eventName);
        if(eventName.equals("onConferenceLeft")) {
            if (receiver != null) {
                getContext().unregisterReceiver(receiver);
                receiver = null;
            }
        }
    }
}
