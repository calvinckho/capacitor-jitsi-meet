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
        google()
        jcenter()
        maven {
            url "https://github.com/jitsi/jitsi-maven-repository/raw/master/releases"
        }
    }
}
```

6. In your android/app/build.gradle file, add Java 1.8 compatibility support

```gradle
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    ...
```

In older capacitor versions, replace the line implementation 'ionic-team:capacitor-android:1+' with:

```gradle
dependencies {
    // (other dependencies)
    implementation project(':capacitor-android')
}
```

7. In older capacitor versions, if not already created, follow the [Ionic doc](https://capacitorjs.com/docs/android/updating#from-1-5-1-to-2-0-0) to create common variables.

 Create an android/variables.gradle file with this content
 ```gradle
 ext {
   minSdkVersion = 23
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

9. Build it in Android Studio

```
ionic capacitor open android
```

10. If upgrading from previous versions of this plugin you may receive this error: `Error: Unfortunately you can't have non-Gradle Java modules and > Android-Gradle modules in one project`. Follow these steps to resolve it:

   a. In Android Studio Go to File -> Invalidate Caches/Restart.
   
   b. Close the project.
   
   c. Go to project folder and delete .idea folder.
   
   d. Delete YourProjectName.iml in project folder.
   
   e. Delete app.iml in app folder.
   
   f. run `npx cap sync && npx cap open android` 

### Android SDK Developer Guide

Consult the official Jitsi Android SDK [documentation](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-android-sdk).
