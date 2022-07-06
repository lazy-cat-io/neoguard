import * as neoguard from './lib/neoguard.js';

export const evalString = (text: string) => neoguard.evalString(text);
export const evalFile = (url: string) => neoguard.evalFile(url);
export const generateCaptcha = () => neoguard.generateCaptcha();
export const generateCaptchaWithMax = (n: number) => neoguard.generateCaptcha(n);
