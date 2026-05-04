// IDOR
function demo(req, res) {
  res.json(invoices[req.params.id]);
  }
