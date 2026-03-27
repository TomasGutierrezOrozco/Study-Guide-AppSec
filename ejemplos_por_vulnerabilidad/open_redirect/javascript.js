// Open Redirect
function demo(req, res) {
  res.redirect(req.query.next);
  }
