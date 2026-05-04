const http = require('http');

function base64url(input) {
  return Buffer.from(input).toString('base64url');
}

const demoToken = `${base64url(JSON.stringify({ alg: 'none', typ: 'JWT' }))}.${base64url(JSON.stringify({ sub: 'student', role: 'user' }))}.`;

const server = http.createServer((req, res) => {
  const url = new URL(req.url, 'http://localhost:3000');

  if (url.pathname === '/search') {
    const q = url.searchParams.get('q') || '';
    res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
    return res.end(`<h1>Resultados para ${q}</h1>`);
  }

  if (url.pathname === '/admin') {
    const auth = req.headers.authorization || '';
    const token = auth.replace('Bearer ', '');
    if (!token) {
      res.writeHead(401, { 'Content-Type': 'text/plain' });
      return res.end(`Envia Authorization: Bearer ${demoToken}`);
    }
    const payload = JSON.parse(Buffer.from(token.split('.')[1] || '', 'base64url').toString('utf8'));
    if (payload.role === 'admin') {
      res.writeHead(200, { 'Content-Type': 'text/plain' });
      return res.end('admin-panel');
    }
    res.writeHead(403, { 'Content-Type': 'text/plain' });
    return res.end('forbidden');
  }

  res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
  res.end(`
    <h1>Node Lab</h1>
    <p>XSS: <a href="/search?q=test">/search?q=test</a></p>
    <p>JWT: <a href="/admin">/admin</a></p>
    <p>Token demo user: <code>${demoToken}</code></p>
  `);
});

server.listen(3000, '0.0.0.0');
