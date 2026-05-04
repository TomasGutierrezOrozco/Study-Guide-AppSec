// XML External Entity Injection (XXE)
function demo(req, res) {
  parser.parse(req.body,{processEntities:true});
  }
