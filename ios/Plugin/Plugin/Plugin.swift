import Foundation
import Capacitor
import JitsiMeetSDK
import UIKit
/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(Jitsi)
public class Jitsi: CAPPlugin {

    var jitsiMeetViewController: JitsiMeetViewController?

    @objc func joinConference(_ call: CAPPluginCall) {

        let podBundle = Bundle(for: JitsiMeetViewController.self)
        let bundleURL = podBundle.url(forResource: "Plugin", withExtension: "bundle")
        let bundle = Bundle(url: bundleURL!)!

        let storyboard = UIStoryboard(name: "JitsiMeet", bundle: bundle)
        self.jitsiMeetViewController = storyboard.instantiateViewController(withIdentifier: "jitsiMeetStoryBoardID") as? JitsiMeetViewController
        guard let url = call.options["url"] as? String else {
            call.reject("Must provide an url")
            return
        }
        guard let roomName = call.options["roomName"] as? String else {
            call.reject("Must provide a roomName")
            return
        }
        let subject = call.options["subject"] as? String ?? " "

        self.jitsiMeetViewController?.webView = self.webView

        self.jitsiMeetViewController?.options = JitsiMeetConferenceOptions.fromBuilder { (builder) in
            builder.serverURL = URL(string: url)
            builder.room = roomName
            builder.setSubject(subject)

            if let token = call.options["token"] as? String {
                builder.token = token;
            }

            if let isAudioMuted = call.options["startWithAudioMuted"] as? Bool {
                builder.setAudioMuted(isAudioMuted);
            }

            /* if let isAudioOnly = call.options["isAudioOnly"] as? Bool {
                builder.setAudioOnly(isAudioOnly)
            } */

            if let isVideoMuted = call.options["startWithVideoMuted"] as? Bool {
                builder.setVideoMuted(isVideoMuted)
            }

            let displayName = call.options["displayName"] as? String
            let email = call.options["email"] as? String
            let avatarUrlString = call.options["avatarURL"] as? String

            if (displayName != nil || email != nil || avatarUrlString != nil) {
                let avatarUrl = avatarUrlString != nil ? URL(string: avatarUrlString!) : nil
                builder.userInfo = JitsiMeetUserInfo(displayName: displayName, andEmail: email, andAvatar: avatarUrl)
            }

            var callIntegrationEnabled = call.options["callIntegrationEnabled"] as? Bool ?? true
            let userLocale = NSLocale.current as NSLocale
            if  userLocale.countryCode?.contains("CN") ?? false ||
                userLocale.countryCode?.contains("CHN") ?? false ||
                userLocale.countryCode?.contains("MO") ?? false ||
                userLocale.countryCode?.contains("HK") ?? false {
                print("currentLocale is China so we cannot use CallKit.")
                callIntegrationEnabled = false
            }
            builder.setFeatureFlag("call-integration.enabled", withBoolean: callIntegrationEnabled)

            // default PiP is off as the feature is not fully functioning yet, but allow user to overrides it by providing the featureFlag
            builder.setFeatureFlag("pip.enabled", withBoolean: false)

            // if not set in the feature flags above, this will take effect
            if let chatEnabled = call.options["chatEnabled"] as? Bool {
                builder.setFeatureFlag("chat.enabled", withBoolean: chatEnabled)
            }
            if let inviteEnabled = call.options["inviteEnabled"] as? Bool {
                builder.setFeatureFlag("invite.enabled", withBoolean: inviteEnabled)
            }
            if let screenSharingEnabled = call.options["screenSharingEnabled"] as? Bool {
                builder.setFeatureFlag("ios.screensharing.enabled", withBoolean: screenSharingEnabled)
            }
            if let recordingEnabled = call.options["recordingEnabled"] as? Bool {
                builder.setFeatureFlag("ios.recording.enabled", withBoolean: recordingEnabled)
            }
            if let liveStreamingEnabled = call.options["liveStreamingEnabled"] as? Bool {
                builder.setFeatureFlag("live-streaming.enabled", withBoolean: liveStreamingEnabled)
            }

            // setfeatureFlag() provides finer control, and will override some of the settings above
            let featureFlags = call.options["featureFlags"] as? Dictionary<String, Any>

            featureFlags?.forEach { key, value in
                builder.setFeatureFlag(key, withValue: value);
            }

            let configOverrides = call.options["configOverrides"] as? Dictionary<String, Any>
            configOverrides?.forEach { key, value in
                builder.setConfigOverride(key, withValue: value);
            }
        }

        self.jitsiMeetViewController?.delegate = self;

        DispatchQueue.main.async {
            self.bridge?.viewController?.present(self.jitsiMeetViewController!, animated: true, completion: nil);
        }
    }
}

extension Jitsi: JitsiMeetViewControllerDelegate {
    @objc func onConferenceJoined() {
        self.bridge?.triggerWindowJSEvent(eventName: "onConferenceJoined");
    }

    @objc func onConferenceLeft() {
        self.bridge?.triggerWindowJSEvent(eventName: "onConferenceLeft");
    }
}
