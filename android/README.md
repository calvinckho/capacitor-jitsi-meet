# Jitsi Meet Plugin for Android

This plugin uses the Jitsi SDK for android.

### Usage

1. npm install capacitor-jitsi-meet, then use it as a Capacitor Plugin

```
import { Plugins } from '@capacitor/core';

const { Jitsi } = Plugins;
const result = await Jitsi.joinConference({
   roomName: 'room1', // room identifier for the conference
   url: 'https://meet.jit.si' // endpoint of the Jitsi Meet video bridge
});

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

```

2. npx cap sync

3. In your android/build.gradle, add the Maven repository

The repository typically goes into the `build.gradle` file in the root of your project:

```gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url "https://github.com/jitsi/jitsi-maven-repository/raw/master/releases"
        }
    }
}
```

4. In your android/app/build.gradle file, add Java 1.8 compatibility support, and also enable 32bit mode for react-native, since react-native only supports 32bit apps. (If you have a 64bit device, it will not run unless this setting it set)

```gradle
android {
    ...
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a", "x86"
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ...
```

Also, replace the line implementation 'ionic-team:capacitor-android:1+' with:

```gradle
dependencies {
    // (other dependencies)
    implementation project(':capacitor-android')
}
```

5. Build it in Android Studio

### Optional: Adding Glide Support

Starting with SDK version 1.22, a Glide module must be provided by the host app.
This makes it possible to use the Glide image processing library from both the
SDK and the host app itself.

You can use the code in `JitsiGlideModule.java` and adjust the package name.
When building, add the following code in your `app/build.gradle` file, adjusting
the Glide version to match the one in https://github.com/jitsi/jitsi-meet/blob/master/android/build.gradle

```
// Glide
implementation("com.github.bumptech.glide:glide:${glideVersion}") {
    exclude group: "com.android.support", module: "glide"
}
implementation("com.github.bumptech.glide:annotations:${glideVersion}") {
    exclude group: "com.android.support", module: "annotations"
}
```

### Optional: API Documentations and Modifying the Plugin

### JitsiMeetActivity

This class encapsulates a high level API in the form of an Android `Activity`
which displays a single `JitsiMeetView`.

### JitsiMeetView

The `JitsiMeetView` class is the core of Jitsi Meet SDK. It's designed to
display a Jitsi Meet conference (or a welcome page).

#### dispose()

Releases all resources associated with this view. This method MUST be called
when the Activity holding this view is going to be destroyed, usually in the
`onDestroy()` method.

#### getDefaultURL()

Returns the default base URL used to join a conference when a partial URL (e.g.
a room name only) is specified to `loadURLString`/`loadURLObject`. If not set or
if set to `null`, the default built in JavaScript is used: https://meet.jit.si.

#### getListener()

Returns the `JitsiMeetViewListener` instance attached to the view.

#### isPictureInPictureEnabled()

Returns `true` if Picture-in-Picture is enabled; `false`, otherwise. If not
explicitly set (by a preceding `setPictureInPictureEnabled` call), defaults to
`true` if the platform supports Picture-in-Picture natively; `false`, otherwise.

#### isWelcomePageEnabled()

Returns true if the Welcome page is enabled; otherwise, false. If false, a black
empty view will be rendered when not in a conference. Defaults to false.

#### loadURL(URL)

Loads a specific URL which may identify a conference to join. If the specified
URL is null and the Welcome page is enabled, the Welcome page is displayed
instead.

#### loadURLString(String)

Loads a specific URL which may identify a conference to join. If the specified
URL is null and the Welcome page is enabled, the Welcome page is displayed
instead.

#### loadURLObject(Bundle)

Loads a specific URL which may identify a conference to join. The URL is
specified in the form of a Bundle of properties which (1) internally are
sufficient to construct a URL (string) while (2) abstracting the specifics of
constructing the URL away from API clients/consumers. If the specified URL is
null and the Welcome page is enabled, the Welcome page is displayed instead.

Example:

```java
Bundle config = new Bundle();
config.putBoolean("startWithAudioMuted", true);
config.putBoolean("startWithVideoMuted", false);
Bundle urlObject = new Bundle();
urlObject.putBundle("config", config);
urlObject.putString("url", "https://meet.jit.si/Test123");
view.loadURLObject(urlObject);
```

#### setDefaultURL(URL)

Sets the default URL. See `getDefaultURL` for more information.

NOTE: Must be called before (if at all) `loadURL`/`loadURLString` for it to take
effect.

#### setListener(listener)

Sets the given listener (class implementing the `JitsiMeetViewListener`
interface) on the view.

#### setPictureInPictureEnabled(boolean)

Sets whether Picture-in-Picture is enabled. If not set, Jitsi Meet SDK
automatically enables/disables Picture-in-Picture based on native platform
support.

NOTE: Must be called (if at all) before `loadURL`/`loadURLString` for it to take
effect.

#### setWelcomePageEnabled(boolean)

Sets whether the Welcome page is enabled. See `isWelcomePageEnabled` for more
information.

NOTE: Must be called (if at all) before `loadURL`/`loadURLString` for it to take
effect.

### ReactActivityLifecycleCallbacks

This class handles the interaction between `JitsiMeetView` and its enclosing
`Activity`. Generally this shouldn't be consumed by users, because they'd be
using `JitsiMeetActivity` instead, which is already completely integrated.

All its methods are static.

#### onActivityResult(...)

Helper method to handle results of auxiliary activities launched by the SDK.
Should be called from the activity method of the same name.

#### onBackPressed()

Helper method which should be called from the activity's `onBackPressed` method.
If this function returns `true`, it means the action was handled and thus no
extra processing is required; otherwise the app should call the parent's
`onBackPressed` method.

#### onHostDestroy(...)

Helper method which should be called from the activity's `onDestroy` method.

#### onHostResume(...)

Helper method which should be called from the activity's `onResume` or `onStop`
method.

#### onHostStop(...)

Helper method which should be called from the activity's `onSstop` method.

#### onNewIntent(...)

Helper method for integrating the *deep linking* functionality. If your app's
activity is launched in "singleTask" mode this method should be called from the
activity's `onNewIntent` method.

#### onRequestPermissionsResult(...)

Helper method to handle permission requests inside the SDK. It should be called
from the activity method of the same name.

#### onUserLeaveHint()

Helper method for integrating automatic Picture-in-Picture. It should be called
from the activity's `onUserLeaveHint` method.

This is a static method.

#### JitsiMeetViewListener

`JitsiMeetViewListener` provides an interface apps can implement to listen to
the state of the Jitsi Meet conference displayed in a `JitsiMeetView`.

`JitsiMeetViewAdapter`, a default implementation of the
`JitsiMeetViewListener` interface is also provided. Apps may extend the class
instead of implementing the interface in order to minimize boilerplate.

##### onConferenceFailed

Called when a joining a conference was unsuccessful or when there was an error
while in a conference.

The `data` `Map` contains an "error" key describing the error and a "url" key
with the conference URL.

#### onConferenceJoined

Called when a conference was joined.

The `data` `Map` contains a "url" key with the conference URL.

#### onConferenceLeft

Called when a conference was left.

The `data` `Map` contains a "url" key with the conference URL.

#### onConferenceWillJoin

Called before a conference is joined.

The `data` `Map` contains a "url" key with the conference URL.

#### onConferenceWillLeave

Called before a conference is left.

The `data` `Map` contains a "url" key with the conference URL.

#### onLoadConfigError

Called when loading the main configuration file from the Jitsi Meet deployment
fails.

The `data` `Map` contains an "error" key with the error and a "url" key with the
conference URL which necessitated the loading of the configuration file.

## ProGuard rules

When using the SDK on a project some proguard rules have to be added in order
to avoid necessary code being stripped. Add the following to your project's
rules file: https://github.com/jitsi/jitsi-meet/blob/master/android/app/proguard-rules.pro

## Picture-in-Picture

`JitsiMeetView` will automatically adjust its UI when presented in a
Picture-in-Picture style scenario, in a rectangle too small to accommodate its
"full" UI.

Jitsi Meet SDK automatically enables (unless explicitly disabled by a
`setPictureInPictureEnabled(false)` call) Android's native Picture-in-Picture
mode iff the platform is supported i.e. Android >= Oreo.

## Dropbox integration

To setup the Dropbox integration, follow these steps:

1. Add the following to the app's AndroidManifest.xml and change `<APP_KEY>` to
your Dropbox app key:
```
<activity
    android:configChanges="keyboard|orientation"
    android:launchMode="singleTask"
    android:name="com.dropbox.core.android.AuthActivity">
  <intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.BROWSABLE" />
    <category android:name="android.intent.category.DEFAULT" />
    <data android:scheme="db-<APP_KEY>" />
  </intent-filter>
</activity>
```

2. Add the following to the app's strings.xml and change `<APP_KEY>` to your
Dropbox app key:
```
<string name="dropbox_app_key"><APP_KEY></string>
```