# Local File Inclusion (LFI)
def demo(params)
  render plain: File.read(params[:file])
  end
