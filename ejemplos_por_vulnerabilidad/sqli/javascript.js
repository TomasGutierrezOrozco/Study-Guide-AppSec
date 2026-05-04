// SQL Injection (SQLI)
function demo(req, res) {
  const sql=`SELECT * FROM users WHERE id=${req.query.id}`;
  }
