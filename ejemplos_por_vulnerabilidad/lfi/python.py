# Local File Inclusion (LFI)
return open(request.args['file']).read()
