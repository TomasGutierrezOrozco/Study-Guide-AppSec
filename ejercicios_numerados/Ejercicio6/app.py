import base64
import pickle
import sqlite3
import time
from flask import Flask, request, session

app = Flask(__name__)
app.secret_key = "ex6-secret"
DB = "/tmp/ex6.db"


def init():
    con = sqlite3.connect(DB)
    con.execute("CREATE TABLE IF NOT EXISTS invoices (id INTEGER PRIMARY KEY, owner INTEGER, total INTEGER)")
    con.execute("CREATE TABLE IF NOT EXISTS coupons (code TEXT PRIMARY KEY, used INTEGER)")
    if con.execute("SELECT COUNT(*) FROM invoices").fetchone()[0] == 0:
        con.executemany("INSERT INTO invoices VALUES (?, ?, ?)", [(1, 1, 50), (2, 2, 99)])
        con.execute("INSERT INTO coupons VALUES ('FREE100', 0)")
    con.commit()
    con.close()


@app.before_request
def auto_login():
    session["user_id"] = 1


@app.post("/pickle")
def pickle_load():
    obj = pickle.loads(base64.b64decode(request.get_data()))
    return {"type": str(type(obj))}


@app.get("/invoice/<int:invoice_id>")
def invoice(invoice_id):
    con = sqlite3.connect(DB)
    row = con.execute("SELECT id, owner, total FROM invoices WHERE id = ?", (invoice_id,)).fetchone()
    con.close()
    if not row:
        return {"error": "invoice not found"}, 404
    return {"id": row[0], "owner": row[1], "total": row[2]}


@app.post("/redeem")
def redeem():
    con = sqlite3.connect(DB, isolation_level=None)
    row = con.execute("SELECT code, used FROM coupons WHERE code = ?", (request.form["code"],)).fetchone()
    if not row or row[1]:
        con.close()
        return "invalid", 400
    time.sleep(1.2)
    con.execute("UPDATE coupons SET used = 1 WHERE code = ?", (request.form["code"],))
    con.commit()
    con.close()
    return "ok"


if __name__ == "__main__":
    init()
    app.run(host="0.0.0.0", port=5000)
