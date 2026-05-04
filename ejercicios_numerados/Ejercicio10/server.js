const http = require('http');
const users = [{ id: '1', name: 'alice', role: 'user' }, { id: '2', name: 'bob', role: 'admin' }];

function parseJson(req) {
  return new Promise((resolve) => {
    let data = '';
    req.on('data', (c) => data += c);
    req.on('end', () => resolve(data ? JSON.parse(data) : {}));
  });
}

http.createServer(async (req, res) => {
  if (req.url === '/graphql' && req.method === 'POST') {
    const body = await parseJson(req);
    const query = body.query || '';
    if (query.includes('__schema')) {
      return res.end(JSON.stringify({ data: { __schema: { types: ['User', 'Query', 'Mutation'] } } }));
    }
    if (query.includes('user(')) {
      const match = query.match(/user\\(id:\\s*"(.*?)"\\)/);
      const id = match ? match[1] : '1';
      return res.end(JSON.stringify({ data: { user: users.find((u) => u.id === id) } }));
    }
    if (query.includes('updateUser')) {
      users[0].role = 'admin';
      return res.end(JSON.stringify({ data: { updateUser: users[0] } }));
    }
    return res.end(JSON.stringify({ data: null }));
  }
  res.end('Ejercicio10');
}).listen(3000, '0.0.0.0');
