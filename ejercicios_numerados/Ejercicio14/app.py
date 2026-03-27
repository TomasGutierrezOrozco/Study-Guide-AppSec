import binascii
import os
from flask import Flask, request
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad

app = Flask(__name__)
KEY = b"0123456789abcdef"


@app.get("/encrypt")
def encrypt():
    msg = request.args.get("msg", "secret").encode()
    iv = os.urandom(16)
    ct = AES.new(KEY, AES.MODE_CBC, iv).encrypt(pad(msg, 16))
    return {"token": binascii.hexlify(iv + ct).decode()}


@app.get("/oracle")
def oracle():
    raw = binascii.unhexlify(request.args.get("token", ""))
    iv, ct = raw[:16], raw[16:]
    try:
        unpad(AES.new(KEY, AES.MODE_CBC, iv).decrypt(ct), 16)
        return "valid"
    except ValueError:
        return "invalid padding", 403


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
