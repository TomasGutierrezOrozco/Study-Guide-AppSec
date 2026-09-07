# Padding Oracle
def demo():
    try:
        unpad(cipher.decrypt(token), 16)
    except ValueError:
        return 'bad padding', 403
