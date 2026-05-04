// LDAP Injection
public class Example {
  public void demo() throws Exception {
    String filter="(uid="+request.getParameter("user")+")";
      }
}
