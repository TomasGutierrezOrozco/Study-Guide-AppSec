# Insecure Deserialization
def demo(params)
  Marshal.load(request.body.read)
  end
