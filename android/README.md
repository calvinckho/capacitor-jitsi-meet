# Jitsi Meet Plugin for Android

This plugin uses the Jitsi SDK for android. See the plugin [changelog](https://github.com/calvinckho/capacitor-jitsi-meet/blob/master/CHANGELOG.md) for the list of corresponding SDKs used.

### Usage

[Follow installation steps 1-3 here.](https://github.com/calvinckho/capacitor-jitsi-meet#usage)

4. Follow [these steps](https://ionicframework.com/docs/developing/android#project-setup) to add Android to your project

5. Let capacitor sync the projects using either of the following commands

```
npx cap update
```
```
npx cap sync
```

6. In your android/build.gradle, add the Maven repository

```gradle
allprojects {
    repositories {
        maven {
            url "https://github.com/jitsi/jitsi-maven-repository/raw/master/releases"
        }
        google()
        mavenCentral()
        maven { url 'https://www.jitpack.io' }
    }
}
```

7. In your android/app/build.gradle file, add Java 11 compatibility support

```gradle
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
    ...
```

8. Add uses permissions in the main app's AndroidManifest.xml

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="com.mycompany.app">
+     <uses-permission android:name="android.permission.CAMERA" />
+     <uses-permission android:name="android.permission.RECORD_AUDIO" />
</manifest>    
```

9. Update android/variables.gradle file with this content
 ```gradle
 ext {
    minSdkVersion = 23
    compileSdkVersion = 32
    targetSdkVersion = 32
    androidxActivityVersion = '1.4.0'
    androidxAppCompatVersion = '1.4.2'
    androidxCoordinatorLayoutVersion = '1.2.0'
    androidxMaterialVersion = '1.6.1'
    androidxExifInterfaceVersion = '1.3.3'
    firebaseMessagingVersion = '23.0.5'
    playServicesLocationVersion = '20.0.0'
    androidxCoreVersion = '1.8.0'
    androidxFragmentVersion = '1.4.1'
    junitVersion = '4.13.2'
    androidxJunitVersion = '1.1.3'
    androidxEspressoCoreVersion = '3.4.0'
    cordovaAndroidVersion = '10.1.1'
    coreSplashScreenVersion = '1.0.0-rc01'
    androidxWebkitVersion = '1.4.0'
 }
```
In older capacitor versions, if not already created, follow the [Ionic doc](https://capacitorjs.com/docs/android/updating#from-1-5-1-to-2-0-0) to create common variables. In android/build.gradle file, add apply from: "variables.gradle" as shown [here](https://github.com/ionic-team/capacitor/blob/master/android-template/build.gradle#L18).

10. Build it in Android Studio

```
ionic capacitor open android
```

11. If upgrading from previous versions of this plugin you may receive this error: `Error: Unfortunately you can't have non-Gradle Java modules and > Android-Gradle modules in one project`. Follow these steps to resolve it:

   a. In Android Studio Go to File -> Invalidate Caches/Restart.
   
   b. Close the project.
   
   c. Go to project folder and delete .idea folder.
   
   d. Delete YourProjectName.iml in project folder.
   
   e. Delete app.iml in app folder.
   
   f. run `npx cap sync && npx cap open android` 

### Dropbox Integration

See [Jitsi Meet Handbook](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-android-sdk#dropbox-integration).

### Screen Sharing Integration

This is an experimental feature and some users experienced issues on Android. The recommendation is to disable it in production build until a working solution is found.
```
featureFlags: { 'android.screensharing.enabled': false }
```
Report your working solutions [here](https://github.com/calvinckho/capacitor-jitsi-meet/issues/35).

### Android SDK Developer Guide

Consult the official Jitsi Android SDK [documentation](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-android-sdk).
