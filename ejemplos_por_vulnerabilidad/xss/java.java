// Cross-Site Scripting (XSS)
public class Example {
  public void demo() throws Exception {
    response.getWriter().write("<h1>"+request.getParameter("q")+"</h1>");
      }
}
