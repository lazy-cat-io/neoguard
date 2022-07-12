import * as neoguard from "https://cdn.jsdelivr.net/npm/@io.lazy-cat/neoguard@0.0.15/index.ts";

const asCodeBlock = (str: string) =>
  `<pre><code class="language-clojure">${str}</code></pre>`;

const malformedInput = (str: string) => `Malformed input:\n${asCodeBlock(str)}`;

export const evalMessage = (msg: string) => {
  if (msg) {
    try {
      const res = neoguard.evalString(msg);
      console.log("evalMessage", res);
      return asCodeBlock(res);
    } catch (err) {
      const res = malformedInput(msg);
      console.error(res, err);
      return res;
    }
  } else {
    return malformedInput(msg);
  }
};

export const generateCaptcha = () => neoguard.generateCaptcha();
