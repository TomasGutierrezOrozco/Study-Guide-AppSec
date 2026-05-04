// ShellShock
public class Example {
  public void Demo() {
    Process.Start("bash", "-c \"echo $HTTP_USER_AGENT\"");
      }
}
