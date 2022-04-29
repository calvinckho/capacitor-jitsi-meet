# Jitsi Meet Plugin for iOS

This plugin uses the Jitsi SDK.

## Xcode and iOS SDK Compatibility

Each version of the jitsi capacitor plugin uses the latest Jitsi SDK version published by Jitsi. In iOS deployment, the SDK version is important because later SDKs compiled using the latest Xcode would not work if you local Xcode is older. So for iOS deployment, make sure you check your Xcode version and install the plugin version that uses the compatible SDK version.  

See the plugin [changelog](https://github.com/calvinckho/capacitor-jitsi-meet/blob/master/CHANGELOG.md) for plugin versions that are compatible to your local Xcode

### Usage

[Follow installation steps 1-3 here.](https://github.com/calvinckho/capacitor-jitsi-meet#usage)

4. Follow [these steps](https://ionicframework.com/docs/developing/ios#project-setup) to add iOS to your project

5. Let capacitor sync the projects using either of the following commands

```
npx cap update
```
```
npx cap sync
```

6. Turn off Bitcode in the app target as well as the pod targets. In xcode Project Navigator,

```
   i. Pods -> Project -> Pods -> Build Settings -> Enable Bitcode -> No
   
   ii Pods -> Targets -> CapacitorJitsiMeet -> Build Settings -> Enable Bitcode -> No
 ``` 

To automate it during project build, insert these lines of code in the iOS project's Podfile
```
post_install do |installer|
  installer.pods_project.targets.each do |target|
  if target.name == 'CapacitorJitsiMeet' || target.name == 'CapacitorJitsiMeet-Plugin'
    target.build_configurations.each do |config|
      config.build_settings['ENABLE_BITCODE'] = 'NO'
    end
  end
  end
end
```

7. Use Swift 5 for the Capacitor target. In Xcode Project Navigator,

```
    i. Pods -> Targets -> Capacitor -> Build Settings -> Swift Language Version -> Swift 5
```

8. In order for app to properly work in the background, select the "audio" and "voip" background modes.

```
<key>UIBackgroundModes</key>
	<array>
		<string>audio</string>
		<string>voip</string>
	</array>
```

9. Build it in Xcode and deploy it to your device

```
ionic capacitor open ios
```

### Dropbox Integration

See [Jitsi Meet Handbook](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-ios-sdk#dropbox-integration).

### Screen Sharing Integration

See [Jitsi Meet Handbook](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-ios-sdk#screen-sharing-integration).


### iOS SDK Developer Guide

Consult the official Jitsi iOS SDK [documentation](https://jitsi.github.io/handbook/docs/dev-guide/dev-guide-ios-sdk).

