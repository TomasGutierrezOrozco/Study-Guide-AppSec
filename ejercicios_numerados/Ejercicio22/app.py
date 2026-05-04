import base64
import json
from flask import Flask, request

app = Flask(__name__)
users = [{"username": "alice", "role": "user"}]
profile = {"theme": "light", "role": "user"}


@app.post("/login")
def login():
    data = request.get_json()
    ok = data and ((isinstance(data.get("username"), dict)) or data.get("username") == "alice")
    return {"ok": bool(ok)}


@app.post("/profile")
def bind_profile():
    profile.update(request.get_json() or {})
    return profile


@app.get("/admin")
def admin():
    token = request.headers.get("Authorization", "").replace("Bearer ", "")
    payload = json.loads(base64.urlsafe_b64decode(token.split(".")[1] + "==")) if token else {}
    return ("admin-ok" if payload.get("role") == "admin" else "forbidden")


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
