// LaTeX Injection
function demo(req, res) {
  const tex=`\\input{${req.query.name}}`;
  }
