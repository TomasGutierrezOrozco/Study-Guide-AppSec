// ShellShock
public class Example {
  public void demo() throws Exception {
    new ProcessBuilder("bash","-c","echo $HTTP_USER_AGENT").start();
      }
}
