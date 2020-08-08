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
            console.log('the web implementation is not available. Please use Jitsi Meet API to implement Jitsi in web app');
            return options;
        });
    }
}
const Jitsi = new JitsiWeb();
export { Jitsi };
// Register as a web plugin
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(Jitsi);
//# sourceMappingURL=web.js.map