import { WebPlugin } from '@capacitor/core';
import { JitsiPlugin } from './definitions';
export declare class JitsiWeb extends WebPlugin implements JitsiPlugin {
    constructor();
    joinConference(options: {
        roomName: string;
        url: string;
    }): Promise<{
        roomName: string;
        url: string;
    }>;
}
declare const Jitsi: JitsiWeb;
export { Jitsi };
