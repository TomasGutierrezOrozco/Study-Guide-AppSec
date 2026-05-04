// XPath Injection
public class Example {
  public void demo() throws Exception {
    String expr="//user[name='"+request.getParameter("user")+"']";
      }
}
