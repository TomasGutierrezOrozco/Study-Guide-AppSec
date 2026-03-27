// Cross-Site Request Forgery (CSRF)
public class Example {
  public void Demo() {
    if (Request.Method == "POST") ChangeEmail(Request.Form["email"]);
      }
}
