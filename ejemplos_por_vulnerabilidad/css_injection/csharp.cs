// CSS Injection (CSSI)
public class Example {
  public void Demo() {
    Response.Write("<style>" + Request.Query["css"] + "</style>");
      }
}
