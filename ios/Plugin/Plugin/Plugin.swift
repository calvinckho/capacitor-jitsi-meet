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

        self.jitsiMeetViewController.url = url + "/" + roomName;
        self.jitsiMeetViewController.channelLastN = call.options["channelLastN"] as? String ?? "-1";
        self.jitsiMeetViewController.startWithAudioMuted = call.options["startWithAudioMuted"] as? Bool ?? false;
        self.jitsiMeetViewController.startWithVideoMuted = call.options["startWithVideoMuted"] as? Bool ?? false;

        self.jitsiMeetViewController.delegate = self;
        
        DispatchQueue.main.async {
            self.bridge.viewController.present(self.jitsiMeetViewController, animated: true, completion: {
                call.resolve([
                    "success": true
                    ])
            });
        }
    }
}

extension Jitsi: JitsiMeetViewControllerDelegate {
    @objc func onConferenceJoined() {
        self.bridge.triggerWindowJSEvent(eventName: "onConferenceJoined");
    }

    @objc func onConferenceLeft() {
        self.bridge.triggerWindowJSEvent(eventName: "onConferenceLeft");
    }
}
