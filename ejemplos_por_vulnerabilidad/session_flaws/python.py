# Session Puzzling / Fixation / Variable Overloading
if 'sid' in request.args: session.sid=request.args['sid']
session.update(request.args)
