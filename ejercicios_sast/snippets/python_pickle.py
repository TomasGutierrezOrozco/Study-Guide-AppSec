import pickle
from flask import Flask, request

app = Flask(__name__)

@app.post("/import")
def do_import():
    obj = pickle.loads(request.data)
    return {"items": len(obj)}
