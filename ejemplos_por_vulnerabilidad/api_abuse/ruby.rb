# API Abuse
def demo(params)
  render json: (1..params.fetch(:limit, 1_000_000).to_i).to_a
  end
