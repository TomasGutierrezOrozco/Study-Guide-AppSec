// Server-Side Template Injection (SSTI)
public class Example {
  public void Demo() {
    return Razor.Parse(Request.Query["tpl"]);
      }
}
