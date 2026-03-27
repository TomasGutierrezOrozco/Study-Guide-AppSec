# Cross-Site Scripting (XSS)
return f"<h1>{request.args.get('q','')}</h1>"
