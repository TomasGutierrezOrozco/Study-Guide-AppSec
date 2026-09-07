# Cross-Site Scripting (XSS)
def demo():
    return f"<h1>{request.args.get('q', '')}</h1>"
