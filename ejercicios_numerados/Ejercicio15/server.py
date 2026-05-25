from http.server import BaseHTTPRequestHandler, HTTPServer
from pathlib import Path

ROOT = Path("/dav")


class Handler(BaseHTTPRequestHandler):
    def do_OPTIONS(self):
        self.send_response(200)
        self.send_header("DAV", "1,2")
        self.send_header("Allow", "OPTIONS, GET, PUT, DELETE, PROPFIND")
        self.end_headers()

    def do_PROPFIND(self):
        self.send_response(207)
        self.end_headers()
        self.wfile.write(b"<multistatus><response>/index.txt</response></multistatus>")

    def do_GET(self):
        target = ROOT / self.path.lstrip("/")
        if not target.is_file():
            self.send_response(404)
            self.end_headers()
            self.wfile.write(b"File not found")
            return
        self.send_response(200)
        self.end_headers()
        self.wfile.write(target.read_bytes())

    def do_PUT(self):
        target = ROOT / self.path.lstrip("/")
        length = int(self.headers.get("Content-Length", "0"))
        target.write_bytes(self.rfile.read(length))
        self.send_response(201)
        self.end_headers()

    def do_DELETE(self):
        target = ROOT / self.path.lstrip("/")
        if target.exists():
            target.unlink()
        self.send_response(204)
        self.end_headers()


HTTPServer(("0.0.0.0", 8080), Handler).serve_forever()
