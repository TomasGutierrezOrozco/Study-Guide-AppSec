from pathlib import Path
from flask import Flask, request, redirect

app = Flask(__name__)
UPLOADS = Path("/tmp/ex19")
UPLOADS.mkdir(exist_ok=True)


@app.get("/")
def home():
    return "Ejercicio19"


@app.get("/read")
def read():
    return Path(request.args.get("file", __file__)).read_text(encoding="utf-8", errors="ignore")


@app.get("/jump")
def jump():
    return redirect(request.args.get("next", "/"))


@app.post("/upload")
def upload():
    f = request.files["file"]
    target = UPLOADS / f.filename
    f.save(target)
    return {"saved": str(target)}


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
