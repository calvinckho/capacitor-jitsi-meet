import { WebPlugin } from '@capacitor/core';
import { JitsiPlugin } from './definitions';
export declare class JitsiWeb extends WebPlugin implements JitsiPlugin {
    constructor();
    joinConference(options: {
        roomName: string;
        url: string;
        channelLastN: string;
        startWithAudioMuted: boolean;
        startWithVideoMuted: boolean;
    }): Promise<{
        roomName: string;
        url: string;
        channelLastN: string;
        startWithAudioMuted: boolean;
        startWithVideoMuted: boolean;
    }>;
}
declare const Jitsi: JitsiWeb;
export { Jitsi };
