// Open Redirect
public class Example {
  public void Demo() {
    return Redirect(Request.Query["next"]);
      }
}
