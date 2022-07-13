import * as neoguard from './lib/neoguard.js';

export const evalString = (text: string) => neoguard.evalString(text);
export const evalFile = (url: string) => neoguard.evalFile(url);
export const generateCaptcha = () => neoguard.generateCaptcha();
export const generateCaptchaWithLimit = (n: number) => neoguard.generateCaptcha(n);
export const asMarkdownCodeBlock = (s: string) => neoguard.asMarkdownCodeBlock(s);
export const asMarkdownCodeBlockWithPrefix = (prefix: string, s: string) => neoguard.asMarkdownCodeBlock(prefix, s);
