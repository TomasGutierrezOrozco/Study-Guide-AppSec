// AXFR Full Zone Transfer
public class Example {
  public void demo() throws Exception {
    new ProcessBuilder("dig","axfr",request.getParameter("domain")).start();
      }
}
