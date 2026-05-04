// Prototype Pollution
function demo(req, res) {
  Object.assign(config,req.body);
  }
