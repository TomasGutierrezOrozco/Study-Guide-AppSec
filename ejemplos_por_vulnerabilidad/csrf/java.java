// Cross-Site Request Forgery (CSRF)
public class Example {
  public void demo() throws Exception {
    if(request.getMethod().equals("POST")){changeEmail(request.getParameter("email"));}
      }
}
