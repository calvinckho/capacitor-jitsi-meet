# Jitsi Meet Plugin for Android

This plugin uses the Jitsi SDK for android. See the plugin [changelog](https://github.com/calvinckho/capacitor-jitsi-meet/blob/master/CHANGELOG.md) for the list of corresponding SDKs used.

### Usage

1. npm install capacitor-jitsi-meet, then use it as a Capacitor Plugin

```javascript
// On Capacitor 3
import { Jitsi } from 'capacitor-jitsi-meet';

// On Capacitor 2
import { Plugins } from '@capacitor/core';
import 'capacitor-jitsi-meet';

const { Jitsi } = Plugins;
```

```javascript
const result: any = await Jitsi.joinConference({
   roomName: 'room1', // room identifier for the conference
   url: 'https://meet.jit.si', // endpoint of the Jitsi Meet video bridge
   token: string, // jwt authentication token
   displayName: string, // user's display name
   email: string, // user's email
   avatarURL: string, // user's avatar url
   channelLastN: string, // last N participants allowed to join
   startWithAudioMuted: true, // start with audio muted
   startWithVideoMuted: false, // start with video muted
   chatEnabled: false, // enable Chat feature
   inviteEnabled: false, // enable Invitation feature
   callIntegrationEnabled: true, // enable call integration (CallKit on iOS, ConnectionService on Android)
});
console.log("join status", result.success);

window.addEventListener('onConferenceJoined', () => {
    // do things here
});
window.addEventListener('onConferenceLeft', () => {
    // do things here
});

const result: any = await Jitsi.leaveConference();
console.log("leave status": result.success);

```

2. npx cap sync

3. In your android/build.gradle, add the Maven repository

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

4. In your android/app/build.gradle file, add Java 1.8 compatibility support

```gradle
android {
    ...
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

5. No need to register the plugin in your main Activity anymore.

6. For 2.0+, follow the [Ionic doc](https://capacitorjs.com/docs/android/updating#from-1-5-1-to-2-0-0) to create common variables.

 Create a android/variables.gradle file with this content
 ```gradle
 ext {
   minSdkVersion = 21
   compileSdkVersion = 29
   targetSdkVersion = 29
   androidxAppCompatVersion = '1.1.0'
   androidxCoreVersion =  '1.2.0'
   androidxMaterialVersion =  '1.1.0-rc02'
   androidxBrowserVersion =  '1.2.0'
   androidxLocalbroadcastmanagerVersion =  '1.0.0'
   firebaseMessagingVersion =  '20.1.2'
   playServicesLocationVersion =  '17.0.0'
   junitVersion =  '4.12'
   androidxJunitVersion =  '1.1.1'
   androidxEspressoCoreVersion =  '3.2.0'
   cordovaAndroidVersion =  '7.0.0'
 }
```
 In android/build.gradle file, add apply from: "variables.gradle" as shown [here](https://github.com/ionic-team/capacitor/blob/master/android-template/build.gradle#L18).

7. Build it in Android Studio

8. If upgrading from previous versions of this plugin you may receive this error: `Error: Unfortunately you can't have non-Gradle Java modules and > Android-Gradle modules in one project`. Follow these steps to resolve it:

   a. In Android Studio Go to File -> Invalidate Caches/Restart.
   
   b. Close the project.
   
   c. Go to project folder and delete .idea folder.
   
   d. Delete YourProjectName.iml in project folder.
   
   e. Delete app.iml in app folder.
   
   f. run `npx cap sync && npx cap open android` 

### Android SDK Developer Guide

Consult the official Jitsi Android SDK [documentation](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-android-sdk).
