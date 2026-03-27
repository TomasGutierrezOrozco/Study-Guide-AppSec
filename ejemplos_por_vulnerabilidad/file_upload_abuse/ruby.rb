# File Upload Abuse
def demo(params)
  File.binwrite("uploads/#{params[:file].original_filename}", params[:file].read)
  end
