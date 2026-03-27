// Server-Side Template Injection (SSTI)
function demo(req, res) {
  res.send(ejs.render(req.query.tpl,{}));
  }
