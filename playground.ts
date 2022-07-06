import {evalString, generateCaptcha, generateCaptchaWithMax} from "./index.ts";

console.log("evalString: correct samples");
["(+ 1 2)", "(inc 41)"].map((s) => console.log(s, "=>", evalString(s)))

console.log("\n")
console.log("evalString: malformed samples");
["", "(+ 1", "(foo 41)"].map((s) => {
  try {
    const res = evalString(s);
    console.log(s, "=>", res)
  } catch (err) {
    console.log(s, "=>", err.message)
  }
})

console.log("\n")
console.log("generateCaptcha:")
for (let n = 0; n < 5; n++) {
  console.log("=>", generateCaptcha())
}

console.log("\n")
console.log("generateCaptchaWithMax:")
for (let n = 0; n < 25; n = n + 5) {
  console.log("max", n, "=>", generateCaptchaWithMax(n))
}
