# API Abuse
def demo():
    return {'items': list(range(int(request.args.get('limit', '1000000'))))}
