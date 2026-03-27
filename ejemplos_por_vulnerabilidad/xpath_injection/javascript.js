// XPath Injection
function demo(req, res) {
  const expr=`//user[name='${req.query.user}']`;
  }
