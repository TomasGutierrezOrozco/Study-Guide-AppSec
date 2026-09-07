# CSS Injection (CSSI)
def demo():
    return f'<style>{request.args.get("css", "body{}")}</style>'
