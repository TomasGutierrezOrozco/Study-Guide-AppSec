// File Upload Abuse
function demo(req, res) {
  fs.writeFileSync('uploads/'+req.files.file.name,req.files.file.data);
  }
