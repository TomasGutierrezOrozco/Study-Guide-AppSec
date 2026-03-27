// LaTeX Injection
public class Example {
  public void Demo() {
    var tex = "\\input{" + Request.Query["name"] + "}";
      }
}
