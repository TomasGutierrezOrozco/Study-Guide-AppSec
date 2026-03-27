// Padding Oracle
public class Example {
  public void demo() throws Exception {
    try{cipher.doFinal(token);}catch(BadPaddingException e){response.sendError(403,"bad padding");}
      }
}
