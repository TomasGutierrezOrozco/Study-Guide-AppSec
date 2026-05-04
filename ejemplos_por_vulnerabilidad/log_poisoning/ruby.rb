# Log Poisoning (LFI a RCE)
def demo(params)
  File.write('access.log', request.user_agent, mode: 'a')
  render plain: File.read(params[:page])
  end
