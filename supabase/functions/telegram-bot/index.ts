import { serve } from "https://deno.land/std@0.140.0/http/server.ts";

import { handle, token } from "./bot.ts";

const handler = async (req: Request) => {
  try {
    const url = new URL(req.url);
    console.log("req", req);
    if (url.searchParams.get("token") !== token) {
      return new Response("Not Allowed", { status: 405 });
    }
    const res = await handle(req);
    console.log("res", res);
    return res;
  } catch (err) {
    console.error(err);
  }
};

serve(handler);
