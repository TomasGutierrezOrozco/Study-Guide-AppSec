// Cross-Site Scripting (XSS)
function demo(req, res) {
  res.send(`<h1>${req.query.q}</h1>`);
  }
