// CSS Injection (CSSI)
function demo(req, res) {
  res.send(`<style>${req.query.css}</style>`);
  }
