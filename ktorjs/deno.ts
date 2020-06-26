import { serve } from 'https://deno.land/std/http/server.ts';
const server = serve({ port: 3000 });
for await (const req of server) {
    console.log('Request from client');
    req.respond({ body: 'Hello World!' });
}