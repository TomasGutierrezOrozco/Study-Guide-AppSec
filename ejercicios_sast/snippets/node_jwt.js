app.get('/admin', (req, res) => {
  const token = req.headers.authorization.replace('Bearer ', '');
  const payload = JSON.parse(Buffer.from(token.split('.')[1], 'base64url').toString());
  if (payload.role === 'admin') {
    return res.send('ok');
  }
  res.status(403).send('forbidden');
});
