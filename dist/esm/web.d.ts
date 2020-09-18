import { WebPlugin } from '@capacitor/core';
import { JitsiPlugin } from './definitions';
export declare class JitsiWeb extends WebPlugin implements JitsiPlugin {
    constructor();
    joinConference(options: {
        roomName: string;
        url: string;
        token: string;
        channelLastN: string;
        startWithAudioMuted: boolean;
        startWithVideoMuted: boolean;
        chatEnabled: boolean;
        inviteEnabled: boolean;
    }): Promise<{
        roomName: string;
        url: string;
        token: string;
        channelLastN: string;
        startWithAudioMuted: boolean;
        startWithVideoMuted: boolean;
        chatEnabled: boolean;
        inviteEnabled: boolean;
    }>;
}
declare const Jitsi: JitsiWeb;
export { Jitsi };
