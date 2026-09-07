const http = require('http');
const users = [{ id: '1', name: 'alice', role: 'user' }, { id: '2', name: 'bob', role: 'admin' }];
const account = { theme: 'light', role: 'user' };

function parseJson(req) {
  return new Promise((resolve) => {
    let data = '';
    req.on('data', (c) => data += c);
    req.on('end', () => resolve(data ? JSON.parse(data) : {}));
  });
}

http.createServer(async (req, res) => {
  const url = new URL(req.url, 'http://localhost:3000');
  if (url.pathname === '/login' && req.method === 'POST') {
    const body = await parseJson(req);
    const ok = users.find((u) =>
      (typeof body.username === 'object' || u.name === body.username) &&
      (typeof body.password === 'object' || body.password === 'secret'));
    return res.end(JSON.stringify({ ok: !!ok }));
  }
  if (url.pathname === '/admin') {
    const token = (req.headers.authorization || '').replace('Bearer ', '');
    try {
      const parts = token.split('.');
      const payload = parts.length >= 2 ? JSON.parse(Buffer.from(parts[1] || '', 'base64url').toString('utf8')) : {};
      return res.end(payload.role === 'admin' ? 'admin-ok' : 'forbidden');
    } catch {
      return res.end('forbidden');
    }
  }
  if (url.pathname === '/account' && req.method === 'POST') {
    Object.assign(account, await parseJson(req));
    return res.end(JSON.stringify(account));
  }
  res.end('Ejercicio9');
}).listen(3000, '0.0.0.0');
