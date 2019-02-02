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
        guard let roomName = call.options["roomName"] as? String else {
            call.reject("Must provide a roomName")
            return
        }
        
        let podBundle = Bundle(for: JitsiMeetViewController.self)
        let bundleURL = podBundle.url(forResource: "Plugin", withExtension: "bundle")
        let bundle = Bundle(url: bundleURL!)!
        
        let storyboard = UIStoryboard(name: "JitsiMeet", bundle: bundle)
        self.jitsiMeetViewController = storyboard.instantiateViewController(withIdentifier: "jitsiMeetStoryBoardID") as? JitsiMeetViewController
        
        let url = call.options["url"] as! String
        let fullurl = url + "/" + roomName;
        
        self.jitsiMeetViewController.url = fullurl;
        
        DispatchQueue.main.async {
            self.bridge.viewController.present(self.jitsiMeetViewController, animated: true, completion: {
                call.resolve()
            });
        }
    }
}
