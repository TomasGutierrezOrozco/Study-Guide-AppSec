# Mass Assignment
def demo(params)
  user.update(params.require(:user).permit!)
  end
