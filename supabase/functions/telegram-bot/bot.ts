import { Bot, webhookCallback } from "https://deno.land/x/grammy@v1.9.0/mod.ts";

import { evalMessage, generateCaptcha } from "./utils.ts";

export const token = Deno.env.get("NEOGUARD_TELEGRAM_BOT_TOKEN") ?? "";

const bot = new Bot(token);

bot.command("start", (ctx) => {
  console.log("start", ctx);
  return ctx.reply("Welcome! Up and running.");
});

bot.command("captcha", (ctx) => {
  console.log("captcha", ctx);
  const captcha = generateCaptcha();
  console.log("generated captcha", captcha);
  return ctx.reply(JSON.stringify(captcha));
});

bot.on("message:text", (ctx) => {
  console.log("message:text", ctx);
  const msg = ctx.message.text;
  const res = evalMessage(msg);
  return ctx.reply(res, {
    reply_to_message_id: ctx.message.message_id,
    parse_mode: "MarkdownV2",
  });
});

bot.on(
  "edited_message",
  (ctx) => {
    console.log("edited_message", ctx);
    const msg = ctx.editedMessage.text;
    if (msg) {
      const res = evalMessage(msg);
      return ctx.reply(res, {
        reply_to_message_id: ctx.editedMessage.message_id,
        parse_mode: "MarkdownV2",
      });
    }
  },
);

export const handle = webhookCallback(bot, "std/http");
