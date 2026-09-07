# Server-Side Request Forgery (SSRF)
def demo():
    return requests.get(request.args['url']).text
