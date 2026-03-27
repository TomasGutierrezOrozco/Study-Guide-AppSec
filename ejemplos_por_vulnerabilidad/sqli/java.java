// SQL Injection (SQLI)
public class Example {
  public void demo() throws Exception {
    String sql="SELECT * FROM users WHERE id="+request.getParameter("id");
      }
}
