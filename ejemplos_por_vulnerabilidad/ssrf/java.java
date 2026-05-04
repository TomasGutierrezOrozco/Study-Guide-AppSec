// Server-Side Request Forgery (SSRF)
public class Example {
  public void demo() throws Exception {
    new URL(request.getParameter("url")).openConnection().getInputStream();
      }
}
