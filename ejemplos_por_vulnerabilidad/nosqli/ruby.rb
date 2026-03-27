# NoSQL Injection
def demo(params)
  User.where(params.permit!).first
  end
