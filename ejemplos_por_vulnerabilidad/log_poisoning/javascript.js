// Log Poisoning (LFI a RCE)
function demo(req, res) {
  fs.appendFileSync('access.log',req.headers['user-agent']+'\n');
  res.send(fs.readFileSync(req.query.page,'utf8'));
  }
