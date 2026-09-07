# Local File Inclusion (LFI)
def demo():
    return open(request.args['file']).read()
