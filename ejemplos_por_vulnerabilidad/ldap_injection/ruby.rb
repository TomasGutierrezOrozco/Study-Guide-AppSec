# LDAP Injection
def demo(params)
  filter = "(uid=#{params[:user]})"
  end
