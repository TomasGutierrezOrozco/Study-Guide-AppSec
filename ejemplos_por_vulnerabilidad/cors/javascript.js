// CORS Misconfiguration
function demo(req, res) {
  res.setHeader('Access-Control-Allow-Origin','*');res.setHeader('Access-Control-Allow-Credentials','true');
  }
