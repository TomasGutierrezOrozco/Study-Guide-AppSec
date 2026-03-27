// IDOR
public class Example {
  public void Demo() {
    return Json(invoices[int.Parse(Request.Query["id"])]);
      }
}
