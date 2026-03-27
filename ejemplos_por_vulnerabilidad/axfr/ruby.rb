# AXFR Full Zone Transfer
def demo(params)
  system("dig axfr #{params[:domain]}")
  end
