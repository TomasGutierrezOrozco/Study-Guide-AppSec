// NoSQL Injection
function demo(req, res) {
  db.users.findOne(req.body);
  }
