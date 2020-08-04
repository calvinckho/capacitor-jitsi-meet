import { WebPlugin } from '@capacitor/core';
import { JitsiPlugin } from './definitions';

export class JitsiWeb extends WebPlugin implements JitsiPlugin {
  constructor() {
    super({
      name: 'Jitsi',
      platforms: ['web']
    });
  }

  async joinConference(options: {
      roomName: string, url: string,
      channelLastN: string;
      startWithAudioMuted: boolean;
      startWithVideoMuted: boolean;
      chatEnabled: boolean;
      inviteEnabled: boolean;
  }): Promise<{
      roomName: string,
      url: string,
      channelLastN: string;
      startWithAudioMuted: boolean;
      startWithVideoMuted: boolean;
      chatEnabled: boolean;
      inviteEnabled: boolean;
  }> {
      console.log('the web implementation is not available. Please use Jitsi Meet API to implement Jitsi in web app');
      return options;
  }
}

const Jitsi = new JitsiWeb();

export { Jitsi };

// Register as a web plugin
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(Jitsi);
