# Open Redirect
def demo(params)
  redirect_to params[:next], allow_other_host: true
  end
