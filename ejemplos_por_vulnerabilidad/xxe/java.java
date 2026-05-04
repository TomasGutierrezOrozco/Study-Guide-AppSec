// XML External Entity Injection (XXE)
public class Example {
  public void demo() throws Exception {
    DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(request.getInputStream());
      }
}
