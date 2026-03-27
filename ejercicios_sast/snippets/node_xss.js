app.get('/hello', (req, res) => {
  const msg = req.query.msg || '';
  res.send(`<div>${msg}</div>`);
});
