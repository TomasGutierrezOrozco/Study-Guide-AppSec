// LDAP Injection
public class Example {
  public void Demo() {
    var filter = "(uid=" + Request.Query["user"] + ")";
      }
}
