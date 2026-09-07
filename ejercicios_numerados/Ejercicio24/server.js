const http = require('http');
const invoices = [{ id: '1', owner: 'alice', total: 50 }, { id: '2', owner: 'bob', total: 90 }];

function parseJson(req) {
  return new Promise((resolve) => {
    let data = '';
    req.on('data', (c) => data += c);
    req.on('end', () => resolve(data ? JSON.parse(data) : {}));
  });
}

http.createServer(async (req, res) => {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Credentials', 'true');
  if (req.url === '/graphql' && req.method === 'POST') {
    const body = await parseJson(req);
    const query = body.query || '';
    if (query.includes('__schema')) {
      return res.end(JSON.stringify({ data: { __schema: { types: ['Invoice', 'Query'] } } }));
    }
    if (query.includes('invoice(')) {
      const match = query.match(/invoice\(\s*id:\s*["']?(\w+)["']?\s*\)/);
      const id = match ? match[1] : '1';
      return res.end(JSON.stringify({ data: { invoice: invoices.find((i) => i.id === id) } }));
    }
    return res.end(JSON.stringify({ data: null }));
  }
  res.end('Ejercicio24');
}).listen(3000, '0.0.0.0');
