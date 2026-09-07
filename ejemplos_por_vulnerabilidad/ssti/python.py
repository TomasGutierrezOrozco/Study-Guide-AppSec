# Server-Side Template Injection (SSTI)
def demo():
    return render_template_string(request.args['tpl'])
