import sqlite3
import time
from flask import Flask, request, session, redirect

app = Flask(__name__)
app.secret_key = "dev-secret"
DB = "/tmp/study_python.db"


def init_db():
    conn = sqlite3.connect(DB)
    conn.execute("CREATE TABLE IF NOT EXISTS invoices (id INTEGER PRIMARY KEY, owner_id INTEGER, total INTEGER)")
    conn.execute("CREATE TABLE IF NOT EXISTS coupons (code TEXT PRIMARY KEY, used INTEGER)")
    conn.execute("CREATE TABLE IF NOT EXISTS balances (user_id INTEGER PRIMARY KEY, credit INTEGER)")
    if conn.execute("SELECT COUNT(*) FROM invoices").fetchone()[0] == 0:
        conn.executemany("INSERT INTO invoices (id, owner_id, total) VALUES (?, ?, ?)", [(1001, 1, 50), (1002, 2, 75)])
    if conn.execute("SELECT COUNT(*) FROM coupons").fetchone()[0] == 0:
        conn.execute("INSERT INTO coupons (code, used) VALUES ('FREE100', 0)")
    if conn.execute("SELECT COUNT(*) FROM balances").fetchone()[0] == 0:
        conn.execute("INSERT INTO balances (user_id, credit) VALUES (1, 0)")
    conn.commit()
    conn.close()


@app.before_request
def auto_login():
    session["user_id"] = 1


@app.get("/")
def index():
    return '<h1>Python Lab</h1><ul><li><a href="/invoice/1001">/invoice/1001</a></li><li><form action="/redeem" method="post"><input name="code" value="FREE100"><button>Redeem</button></form></li></ul>'


@app.get("/invoice/<int:invoice_id>")
def invoice(invoice_id):
    conn = sqlite3.connect(DB)
    row = conn.execute("SELECT id, owner_id, total FROM invoices WHERE id = ?", (invoice_id,)).fetchone()
    conn.close()
    if not row:
        return {"error": "not found"}, 404
    return {"id": row[0], "owner_id": row[1], "total": row[2]}


@app.post("/redeem")
def redeem():
    code = request.form["code"]
    conn = sqlite3.connect(DB, isolation_level=None)
    row = conn.execute("SELECT code, used FROM coupons WHERE code = ?", (code,)).fetchone()
    if not row or row[1]:
        conn.close()
        return "invalid", 400
    time.sleep(1.5)
    conn.execute("UPDATE balances SET credit = credit + 100 WHERE user_id = ?", (session["user_id"],))
    conn.execute("UPDATE coupons SET used = 1 WHERE code = ?", (code,))
    balance = conn.execute("SELECT credit FROM balances WHERE user_id = ?", (session["user_id"],)).fetchone()[0]
    conn.commit()
    conn.close()
    return {"status": "ok", "balance": balance}


@app.get("/balance")
def balance():
    conn = sqlite3.connect(DB)
    value = conn.execute("SELECT credit FROM balances WHERE user_id = 1").fetchone()[0]
    conn.close()
    return {"credit": value}


if __name__ == "__main__":
    init_db()
    app.run(host="0.0.0.0", port=5000, debug=False)
