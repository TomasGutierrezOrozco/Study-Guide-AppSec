// Server-Side Request Forgery (SSRF)
function demo(req, res) {
  fetch(req.query.url).then(r=>r.text()).then(t=>res.send(t));
  }
