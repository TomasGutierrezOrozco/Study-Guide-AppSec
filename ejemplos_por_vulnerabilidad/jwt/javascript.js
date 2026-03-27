// JWT Enumeration and Exploitation
function demo(req, res) {
  const payload=JSON.parse(Buffer.from(token.split('.')[1],'base64url').toString());
  }
