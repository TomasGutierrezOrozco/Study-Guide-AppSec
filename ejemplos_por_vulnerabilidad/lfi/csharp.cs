// Local File Inclusion (LFI)
public class Example {
  public void Demo() {
    var body = File.ReadAllText(Request.Query["file"]);
      }
}
