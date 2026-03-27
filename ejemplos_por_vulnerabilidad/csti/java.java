// Client-Side Template Injection (CSTI)
public class Example {
  public void demo() throws Exception {
    response.getWriter().write("<div>{{"+request.getParameter("expr")+"}}</div>");
      }
}
