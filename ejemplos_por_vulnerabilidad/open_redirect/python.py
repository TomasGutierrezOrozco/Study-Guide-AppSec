# Open Redirect
def demo():
    return redirect(request.args['next'])
