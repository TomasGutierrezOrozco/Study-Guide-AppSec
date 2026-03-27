# Server-Side Template Injection (SSTI)
def demo(params)
  render inline: params[:tpl]
  end
