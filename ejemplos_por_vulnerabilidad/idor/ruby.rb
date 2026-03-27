# IDOR
def demo(params)
  render json: Invoice.find(params[:id])
  end
