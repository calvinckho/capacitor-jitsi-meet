declare global {
    interface PluginRegistry {
        Jitsi?: JitsiPlugin;
    }
}
export interface JitsiPlugin {
    joinConference(options: {
        roomName: string;
        url: string;
        channelLastN: number;
    }): Promise<{
        roomName: string;
        url: string;
        channelLastN: number;
    }>;
}
