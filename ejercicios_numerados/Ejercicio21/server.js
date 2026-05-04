const http = require('http');

let email = 'alice@example.com';

function parseBody(req) {
  return new Promise((resolve) => {
    let data = '';
    req.on('data', (c) => data += c);
    req.on('end', () => resolve(new URLSearchParams(data)));
  });
}

http.createServer(async (req, res) => {
  const url = new URL(req.url, 'http://localhost:3000');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Credentials', 'true');

  if (url.pathname === '/search') {
    return res.end(`<h1>${url.searchParams.get('q') || ''}</h1>`);
  }
  if (url.pathname === '/email' && req.method === 'POST') {
    const body = await parseBody(req);
    email = body.get('email') || email;
    return res.end(`email=${email}`);
  }
  res.end('Ejercicio21');
}).listen(3000, '0.0.0.0');
