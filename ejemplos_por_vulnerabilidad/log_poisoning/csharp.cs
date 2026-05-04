// Log Poisoning (LFI a RCE)
public class Example {
  public void Demo() {
    File.AppendAllText("access.log", Request.Headers["User-Agent"]);
      }
}
