// Client-Side Template Injection (CSTI)
function demo(req, res) {
  res.send(`<div>{{${req.query.expr}}}</div>`);
  }
