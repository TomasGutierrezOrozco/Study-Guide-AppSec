// WebDAV Enumeration and Exploitation
public class Example {
  public void demo() throws Exception {
    if(request.getMethod().equals("PUT")) Files.write(Path.of(request.getRequestURI()), request.getInputStream().readAllBytes());
      }
}
