// Session Puzzling / Fixation / Variable Overloading
public class Example {
  public void Demo() {
    HttpContext.Session.SetString("role", Request.Query["role"]);
      }
}
