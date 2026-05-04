# Server-Side Request Forgery (SSRF)
def demo(params)
  render plain: Net::HTTP.get(URI(params[:url]))
  end
