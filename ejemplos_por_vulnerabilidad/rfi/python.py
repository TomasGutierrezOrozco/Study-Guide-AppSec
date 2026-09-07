# Remote File Inclusion (RFI)
def demo():
    return requests.get(request.args['url']).text
