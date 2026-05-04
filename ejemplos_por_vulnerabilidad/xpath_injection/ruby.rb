# XPath Injection
def demo(params)
  xpath = "//user[name='#{params[:user]}']"
  end
