// Cross-Site Request Forgery (CSRF)
function demo(req, res) {
  app.post('/email',(req,res)=>changeEmail(req.body.email));
  }
