// SQL Truncation
public class Example {
  public void Demo() {
    var username = (Request.Form["username"] ?? "").Substring(0, 8);
      }
}
