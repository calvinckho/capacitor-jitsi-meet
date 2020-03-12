# Jitsi Meet Plugin for iOS

This plugin uses the Jitsi SDK 2.4.2 for iOS.

### Usage

1. npm install capacitor-jitsi-meet, then use it as a Capacitor Plugin

```
import { Plugins } from '@capacitor/core';
import 'capacitor-jitsi-meet';

const { Jitsi } = Plugins;
const result = await Jitsi.joinConference({
   roomName: 'room1', // room identifier for the conference
   url: 'https://meet.jit.si' // endpoint of the Jitsi Meet video bridge
   startWithAudioMuted: true, // start with audio muted
   startWithVideoMuted: false // start with video muted
});

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

```

2. npx cap sync

3. In xcode, turn off Bitcode in the app target as well as the pod targets: 


   i. Choose the Project -> Targets -> Build Settings -> Enable Bitcode -> No
   
   ii Choose Pods -> Targets -> CapacitorJitsiMeet -> Build Settings -> Enable Bitcode -> No
   

4. Some users may need to use Swift 5 for the Capacitor target. In xcode, click on Pods -> Targets -> Capacitor -> Build Settings -> Swift Language Version -> 5

5. Deploy it to your device

### Optional: API Documentations and Modifying the Plugin

### JitsiMeetView class

The `JitsiMeetView` class is the entry point to the Jitsi Meet SDK. It a subclass of
`UIView` which renders a full conference in the designated area.

#### delegate

Property to get/set the `JitsiMeetViewDelegate` on `JitsiMeetView`.

#### defaultURL

Property to get/set the default base URL used to join a conference when a
partial URL (e.g. a room name only) is specified to
`loadURLString:`/`loadURLObject:`. If not set or if set to `nil`, the default
built in JavaScript is used: https://meet.jit.si.

NOTE: Must be set (if at all) before `loadURL:`/`loadURLString:` for it to take
effect.

#### pictureInPictureEnabled

Property to get / set whether Picture-in-Picture is enabled. Defaults to `YES`
if `delegate` implements `enterPictureInPicture:`; otherwise, `NO`.

NOTE: Must be set (if at all) before `loadURL:`/`loadURLString:` for it to take
effect.

#### welcomePageEnabled

Property to get/set whether the Welcome page is enabled. If `NO`, a black empty
view will be rendered when not in a conference. Defaults to `NO`.

NOTE: Must be set (if at all) before `loadURL:`/`loadURLString:` for it to take
effect.

#### loadURL:NSURL

```objc
jitsiMeetView?.loadURL("url": this.url);
```

Loads a specific URL which may identify a conference to join. If the specified
URL is `nil` and the Welcome page is enabled, the Welcome page is displayed
instead.

#### loadURLObject:NSDictionary

```objc
jitsiMeetView?.loadURLObject([
            "config": [
                "startWithAudioMuted": NSNumber(value: false),
                "startWithVideoMuted": NSNumber(value: false)
            ],
            "url": "https://meet.jit.si/test123"
            ]);
```

Loads a specific URL which may identify a conference to join. The URL is
specified in the form of an `NSDictionary` of properties which (1) internally
are sufficient to construct a URL (string) while (2) abstracting the specifics
of constructing the URL away from API clients/consumers. If the specified URL is
`nil` and the Welcome page is enabled, the Welcome page is displayed instead.

#### loadURLString:NSString

```objc
jitsiMeetView?.loadURLString("url": "https://meet.jit.si/test123");
```

Loads a specific URL which may identify a conference to join. If the specified
URL is `nil` and the Welcome page is enabled, the Welcome page is displayed
instead.

#### Universal / deep linking

In order to support Universal / deep linking, `JitsiMeetView` offers 2 class
methods that you app's delegate should call in order for the app to follow those
links.

```objc
-  (BOOL)application:(UIApplication *)application
continueUserActivity:(NSUserActivity *)userActivity
  restorationHandler:(void (^)(NSArray *restorableObjects))restorationHandler
{
  return [JitsiMeetView application:application
               continueUserActivity:userActivity
                 restorationHandler:restorationHandler];
}
```

And also one of the following:

```objc
// See https://developer.apple.com/documentation/uikit/uiapplicationdelegate/1623073-application?language=objc
- (BOOL)application:(UIApplication *)app
            openURL:(NSURL *)url
            options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
  return [JitsiMeetView application:app
                            openURL:url
                            options: options];
}
```

### JitsiMeetViewDelegate

This delegate is optional, and can be set on the `JitsiMeetView` instance using
the `delegate` property.

It provides information about the conference state: was it joined, left, did it
fail?

All methods in this delegate are optional.

#### conferenceJoined

Called when a conference was joined.

The `data` dictionary contains a "url" key with the conference URL.

#### conferenceTerminated

Called when a conference was left.

The `data` dictionary contains an "error" key with the error and a "url" key with the conference URL. If the conference finished gracefully no error key will be present.

#### conferenceWillJoin

Called before a conference is joined.

The `data` dictionary contains a "url" key with the conference URL.

#### enterPictureInPicture

Called when entering Picture-in-Picture is requested by the user. The app should
now activate its Picture-in-Picture implementation (and resize the associated
`JitsiMeetView`. The latter will automatically detect its new size and adjust
its user interface to a variant appropriate for the small size ordinarily
associated with Picture-in-Picture.)

The `data` dictionary is empty.

#### loadConfigError

Called when loading the main configuration file from the Jitsi Meet deployment
fails.

The `data` dictionary contains an "error" key with the error and a "url" key
with the conference URL which necessitated the loading of the configuration
file.

### Picture-in-Picture

`JitsiMeetView` will automatically adjust its UI when presented in a
Picture-in-Picture style scenario, in a rectangle too small to accommodate its
"full" UI.

Jitsi Meet SDK does not currently implement native Picture-in-Picture on iOS. If
desired, apps need to implement non-native Picture-in-Picture themselves and
resize `JitsiMeetView`.

If `pictureInPictureEnabled` is set to `YES` or `delegate` implements
`enterPictureInPicture:`, the in-call toolbar will render a button to afford the
user to request entering Picture-in-Picture.

## Dropbox integration

To setup the Dropbox integration, follow these steps:

1. Add the following to the app's Info.plist and change `<APP_KEY>` to your
Dropbox app key:
```
<key>CFBundleURLTypes</key>
<array>
  <dict>
    <key>CFBundleURLName</key>
    <string></string>
    <key>CFBundleURLSchemes</key>
    <array>
      <string>db-<APP_KEY></string>
    </array>
  </dict>
</array>
<key>LSApplicationQueriesSchemes</key>
<array>
  <string>dbapi-2</string>
  <string>dbapi-8-emm</string>
</array>
```

2. Add the following to the app's `AppDelegate`:
```objc
- (BOOL)application:(UIApplication *)app
            openURL:(NSURL *)url
            options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
  return [JitsiMeetView application:app
                            openURL:url
                            options:options];
}
```
