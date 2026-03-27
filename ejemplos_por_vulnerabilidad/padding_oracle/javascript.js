// Padding Oracle
function demo(req, res) {
  try{decrypt(req.query.token);}catch{res.status(403).send('bad padding');}
  }
