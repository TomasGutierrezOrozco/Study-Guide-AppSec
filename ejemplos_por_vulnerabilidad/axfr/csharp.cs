// AXFR Full Zone Transfer
public class Example {
  public void Demo() {
    Process.Start("dig", "axfr " + Request.Query["domain"]);
      }
}
