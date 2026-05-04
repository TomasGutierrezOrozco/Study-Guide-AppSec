# Remote File Inclusion (RFI)
def demo(params)
  render plain: URI.open(params[:url]).read
  end
