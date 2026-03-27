// Insecure Deserialization
public class Example {
  public void demo() throws Exception {
    new ObjectInputStream(request.getInputStream()).readObject();
      }
}
