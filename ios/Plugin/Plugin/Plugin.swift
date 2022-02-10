import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitor.ionicframework.com/docs/plugins/ios
 */
@objc(Jitsi)
public class Jitsi: CAPPlugin {

    var jitsiMeetViewController: JitsiMeetViewController!

    @objc func joinConference(_ call: CAPPluginCall) {
        guard let url = call.options["url"] as? String else {
                call.reject("Must provide an url")
                return
            }
        guard let roomName = call.options["roomName"] as? String else {
            call.reject("Must provide a roomName")
            return
        }

        let podBundle = Bundle(for: JitsiMeetViewController.self)
        let bundleURL = podBundle.url(forResource: "Plugin", withExtension: "bundle")
        let bundle = Bundle(url: bundleURL!)!

        let storyboard = UIStoryboard(name: "JitsiMeet", bundle: bundle)
        self.jitsiMeetViewController = storyboard.instantiateViewController(withIdentifier: "jitsiMeetStoryBoardID") as? JitsiMeetViewController

        self.jitsiMeetViewController.url = url;
        self.jitsiMeetViewController.token = call.options["token"] as? String ?? nil;
        self.jitsiMeetViewController.roomName = roomName;
        self.jitsiMeetViewController.startWithAudioMuted = call.options["startWithAudioMuted"] as? Bool ?? false;
        self.jitsiMeetViewController.startWithVideoMuted = call.options["startWithVideoMuted"] as? Bool ?? false;
        self.jitsiMeetViewController.chatEnabled = call.options["chatEnabled"] as? Bool ?? false;
        self.jitsiMeetViewController.inviteEnabled = call.options["inviteEnabled"] as? Bool ?? false;
        self.jitsiMeetViewController.email = call.options["email"] as? String ?? nil
        self.jitsiMeetViewController.displayName = call.options["displayName"] as? String ?? nil
        self.jitsiMeetViewController.avatarUrl = call.options["avatarURL"] as? String ?? nil
        self.jitsiMeetViewController.callIntegrationEnabled = call.options["callIntegrationEnabled"] as? Bool ?? true
        self.jitsiMeetViewController.screenSharingEnabled = call.options["screenSharingEnabled"] as? Bool ?? false
        self.jitsiMeetViewController.recordingEnabled = call.options["recordingEnabled"] as? Bool ?? false
        self.jitsiMeetViewController.liveStreamingEnabled = call.options["liveStreamingEnabled"] as? Bool ?? nil
        self.jitsiMeetViewController.delegate = self;

        DispatchQueue.main.async {
            self.bridge?.presentVC(self.jitsiMeetViewController, animated: true, completion: {
                call.resolve([
                    "success": true
                    ])
            });
        }
    }

    @objc func leaveConference(_ call: CAPPluginCall) {
        self.jitsiMeetViewController.delegate = self;
        DispatchQueue.main.async {
            self.bridge?.dismissVC(animated: true, completion: {
                call.resolve([
                    "success": true
                ])
            })
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
