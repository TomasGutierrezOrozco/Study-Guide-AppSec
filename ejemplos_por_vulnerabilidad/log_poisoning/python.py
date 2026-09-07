# Log Poisoning (LFI a RCE)
def demo():
    open('access.log', 'a').write(request.headers.get('User-Agent', '') + '\n')
    return render_template_string(open(request.args['page']).read())
