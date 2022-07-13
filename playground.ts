import {
  asMarkdownCodeBlock,
  asMarkdownCodeBlockWithPrefix,
  evalString,
  generateCaptcha,
  generateCaptchaWithLimit,
} from "./index.ts";

console.log("evalString: correct samples");
["(+ 1 2)", "(inc 41)", '(str "foo-" 42)'].map((s) =>
  console.log(s, "=>", evalString(s))
);

console.log("\n");
console.log("evalString: correct samples [as markdown]");
["(+ 1 2)", "(inc 41)", '(str "foo-" 42)'].map((s) =>
  console.log(s, "=>", asMarkdownCodeBlock(evalString(s)))
);

console.log("\n");
console.log("evalString: malformed samples");
["", "(+ 1", "(foo 41)"].map((s) => {
  try {
    const res = evalString(s);
    console.log(s, "=>", res);
  } catch (err) {
    console.log(s, "=>", err.message);
  }
});

console.log("\n");
console.log("evalString: malformed samples [as markdown]");
["(+ 1", "(foo 41)"].map((s) => {
  try {
    const res = evalString(s);
    console.log(s, "=>", res);
  } catch (_) {
    console.log(s, "=>", asMarkdownCodeBlockWithPrefix("Malformed input:", s));
  }
});

console.log("\n");
console.log("generateCaptcha:");
for (let n = 0; n < 5; n++) {
  console.log("=>", generateCaptcha());
}

console.log("\n");
console.log("generateCaptchaWithLimit:");
for (let n = 0; n < 25; n = n + 5) {
  console.log("max", n, "=>", generateCaptchaWithLimit(n));
}
