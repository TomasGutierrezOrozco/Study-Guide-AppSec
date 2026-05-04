# Cross-Site Scripting (XSS)
def demo(params)
  render html: "<h1>#{params[:q]}</h1>".html_safe
  end
