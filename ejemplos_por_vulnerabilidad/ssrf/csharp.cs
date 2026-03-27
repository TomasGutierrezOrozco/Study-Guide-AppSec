// Server-Side Request Forgery (SSRF)
public class Example {
  public void Demo() {
    var body = new HttpClient().GetStringAsync(url).Result;
      }
}
