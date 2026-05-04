// Local File Inclusion (LFI)
public class Example {
  public void demo() throws Exception {
    Files.readString(Path.of(request.getParameter("file")));
      }
}
