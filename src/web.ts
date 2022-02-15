import { WebPlugin } from '@capacitor/core';
import { JitsiPlugin } from './definitions';

export class JitsiWeb extends WebPlugin implements JitsiPlugin {

  // @ts-ignore
    async joinConference(options: {
        roomName: string;
        url: string;
        token?: string;
        channelLastN?: string;
        displayName?: string;
        subject?: string;
        email?: string;
        avatarURL?: string;
        startWithAudioMuted?: boolean;
        startWithVideoMuted?: boolean;
        chatEnabled?: boolean;
        inviteEnabled?: boolean;
        callIntegrationEnabled?: boolean;
        recordingEnabled?: boolean;
        liveStreamingEnabled?: boolean;
        screenSharingEnabled?: boolean;
        featureFlags?: Map<string, any>;
        configOverrides?: Map<string, any>;
  }): Promise<{
        roomName: string;
        url: string;
        token?: string;
        channelLastN?: string;
        displayName?: string;
        subject?: string;
        email?: string;
        avatarURL?: string;
        startWithAudioMuted?: boolean;
        startWithVideoMuted?: boolean;
        chatEnabled?: boolean;
        inviteEnabled?: boolean;
        callIntegrationEnabled?: boolean;
        recordingEnabled?: boolean;
        liveStreamingEnabled?: boolean;
        screenSharingEnabled?: boolean;
        featureFlags?: Map<string, any>;
        configOverrides?: Map<string, any>;
  }> {
      throw this.unavailable('the web implementation is not available. Please use Jitsi Meet API to implement Jitsi in web app');
  };
    // @ts-ignore
  async leaveConference(options: {}): Promise<{}> {
      throw this.unavailable('the web implementation is not available. Please use Jitsi Meet API to implement Jitsi in web app');
  };
}

const Jitsi = new JitsiWeb();

export { Jitsi };
