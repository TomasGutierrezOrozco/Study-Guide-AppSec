// Local File Inclusion (LFI)
function demo(req, res) {
  res.send(fs.readFileSync(req.query.file,'utf8'));
  }
