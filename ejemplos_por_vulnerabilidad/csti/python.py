# Client-Side Template Injection (CSTI)
return f'<div>{{{{{request.args.get("expr","7*7")}}}}}</div>'
