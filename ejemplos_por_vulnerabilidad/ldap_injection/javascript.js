// LDAP Injection
function demo(req, res) {
  const filter=`(uid=${req.query.user})`;
  }
