// Session Puzzling / Fixation / Variable Overloading
function demo(req, res) {
  req.session.id=req.query.sid;Object.assign(req.session,req.query);
  }
