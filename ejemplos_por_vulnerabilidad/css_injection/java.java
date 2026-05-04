// CSS Injection (CSSI)
public class Example {
  public void demo() throws Exception {
    response.getWriter().write("<style>"+request.getParameter("css")+"</style>");
      }
}
