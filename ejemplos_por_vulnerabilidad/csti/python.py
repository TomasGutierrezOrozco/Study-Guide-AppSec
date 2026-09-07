# Client-Side Template Injection (CSTI)
def demo():
    return f'<div>{{{{{request.args.get("expr", "7*7")}}}}}</div>'
