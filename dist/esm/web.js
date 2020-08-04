var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { WebPlugin } from '@capacitor/core';
export class JitsiWeb extends WebPlugin {
    constructor() {
        super({
            name: 'Jitsi',
            platforms: ['web']
        });
    }
    joinConference(options) {
        return __awaiter(this, void 0, void 0, function* () {
            console.log('this is a sample feature. It uses the default jitsi.meet interface for web implementation using this roomName:', options.roomName);
            window.open('https://meet.jit.si' + options.roomName + ';channelLastN=' + options.channelLastN + ';startWithAudioMuted=' + options.startWithAudioMuted + ';startWithVideoMuted=' + options.startWithVideoMuted, '_blank');
            return options;
        });
    }
}
const Jitsi = new JitsiWeb();
export { Jitsi };
//# sourceMappingURL=web.js.map