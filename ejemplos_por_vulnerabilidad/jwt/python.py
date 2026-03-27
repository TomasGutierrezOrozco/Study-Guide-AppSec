# JWT Enumeration and Exploitation
payload=json.loads(base64.urlsafe_b64decode(token.split('.')[1]+'=='))
