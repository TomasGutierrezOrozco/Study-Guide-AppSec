# XML External Entity Injection (XXE)
def demo(params)
  doc = Nokogiri::XML(request.body.read) { |c| c.noent }
  end
