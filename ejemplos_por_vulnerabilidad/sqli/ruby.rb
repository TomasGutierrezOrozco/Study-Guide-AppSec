# SQL Injection (SQLI)
def demo(params)
  sql = "SELECT * FROM users WHERE id = #{params[:id]}"
  end
