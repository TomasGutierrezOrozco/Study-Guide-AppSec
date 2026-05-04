// Client-Side Template Injection (CSTI)
public class Example {
  public void Demo() {
    Response.Write("<div>{{" + Request.Query["expr"] + "}}</div>");
      }
}
