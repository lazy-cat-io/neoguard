import * as neoguard from "https://cdn.jsdelivr.net/npm/@io.lazy-cat/neoguard@0.0.16/index.ts";

export const evalMessage = (msg: string) => {
  try {
    const res = neoguard.evalString(msg);
    console.log("evalMessage", neoguard.asMarkdownCodeBlock(res));
    return res;
  } catch (err) {
    const res = neoguard.asMarkdownCodeBlockWithPrefix("Malformed input:", msg);
    console.error("evalMessage", res, err);
    return res;
  }
};

export const generateCaptcha = () => neoguard.generateCaptcha();
