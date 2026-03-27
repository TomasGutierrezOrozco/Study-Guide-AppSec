from flask import Flask, request, session

app = Flask(__name__)

@app.post("/profile/email")
def change_email():
    if "user_id" not in session:
        return "auth required", 401
    save_email(session["user_id"], request.form["email"])
    return "ok"
