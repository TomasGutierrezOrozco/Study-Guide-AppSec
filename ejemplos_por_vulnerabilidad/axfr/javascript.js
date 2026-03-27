// AXFR Full Zone Transfer
function demo(req, res) {
  exec(`dig axfr ${req.query.domain}`);
  }
