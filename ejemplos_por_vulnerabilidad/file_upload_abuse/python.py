# File Upload Abuse
request.files['file'].save('uploads/'+request.files['file'].filename)
