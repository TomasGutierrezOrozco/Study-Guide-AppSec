# CSS Injection (CSSI)
return f'<style>{request.args.get("css","body{}")}</style>'
