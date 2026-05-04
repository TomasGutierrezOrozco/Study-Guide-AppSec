# JWT Enumeration and Exploitation
def demo(params)
  payload = JSON.parse(Base64.urlsafe_decode64(token.split('.')[1]))
  end
