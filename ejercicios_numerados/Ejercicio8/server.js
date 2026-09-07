const http = require('http');
const profile = { theme: 'light' };

function parseJson(req) {
  return new Promise((resolve) => {
    let data = '';
    req.on('data', (c) => data += c);
    req.on('end', () => resolve(data ? JSON.parse(data) : {}));
  });
}

function deepMerge(target, source) {
  for (const key of Object.keys(source)) {
    if (typeof source[key] === 'object' && source[key] !== null && !Array.isArray(source[key])) {
      if (!target[key]) target[key] = {};
      deepMerge(target[key], source[key]);
    } else {
      target[key] = source[key];
    }
  }
  return target;
}

http.createServer(async (req, res) => {
  const url = new URL(req.url, 'http://localhost:3000');
  if (url.pathname === '/view') {
    const expr = url.searchParams.get('template') || '{{7*7}}';
    return res.end(`<html><body><script>
      const tpl = "${expr.replace(/"/g, '\\"')}";
      document.body.innerHTML += tpl.replace(/\\{\\{(.*?)\\}\\}/g, (_, code) => eval(code));
    </script></body></html>`);
  }
  if (url.pathname === '/style') {
    return res.end(`<style>${url.searchParams.get('css') || 'body{color:black}'}</style><h1>Styled</h1>`);
  }
  if (url.pathname === '/profile' && req.method === 'POST') {
    const data = await parseJson(req);
    deepMerge(profile, data);
    return res.end(JSON.stringify(profile));
  }
  res.end('Ejercicio8');
}).listen(3000, '0.0.0.0');
