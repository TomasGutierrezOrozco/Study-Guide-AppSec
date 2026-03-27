// API Abuse
function demo(req, res) {
  res.json({items:[...Array(Number(req.query.limit||1000000)).keys()]});
  }
