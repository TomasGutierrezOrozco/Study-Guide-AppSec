// XPath Injection
public class Example {
  public void Demo() {
    var expr = "//user[name='" + Request.Query["user"] + "']";
      }
}
