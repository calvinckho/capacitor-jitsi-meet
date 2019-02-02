package com.capacitor.jitsi.plugin;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import android.content.Context;
import android.util.Log;
import android.content.Intent;
import android.Manifest;

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

        if(url == null) {
            call.reject("Must provide an url");
            return;
        }
        if(roomName == null) {
                    call.reject("Must provide an conference room name");
                    return;
                }
        Log.v(TAG,"display url: "+url);

        Intent intent = new Intent(getActivity(), JitsiActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("roomName",roomName);

        getActivity().startActivity(intent);

        JSObject ret = new JSObject();
        ret.put("result", true);
        call.success(ret);
    }
}
