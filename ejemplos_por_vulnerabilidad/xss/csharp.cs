// Cross-Site Scripting (XSS)
public class Example {
  public void Demo() {
    Response.Write("<h1>" + Request.Query["q"] + "</h1>");
      }
}
