# Client-Side Template Injection (CSTI)
def demo(params)
  render html: "<div>{{#{params[:expr]}}}</div>".html_safe
  end
