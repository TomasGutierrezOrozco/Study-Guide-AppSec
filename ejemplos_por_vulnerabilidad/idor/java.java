// IDOR
public class Example {
  public void demo() throws Exception {
    invoiceService.findById(Long.parseLong(request.getParameter("id")));
      }
}
