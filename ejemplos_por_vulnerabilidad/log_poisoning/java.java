// Log Poisoning (LFI a RCE)
public class Example {
  public void demo() throws Exception {
    logger.info(request.getHeader("User-Agent"));
    Files.readString(Path.of(request.getParameter("page")));
      }
}
