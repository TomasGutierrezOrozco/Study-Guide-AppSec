# CSS Injection (CSSI)
def demo(params)
  render html: "<style>#{params[:css]}</style>".html_safe
  end
