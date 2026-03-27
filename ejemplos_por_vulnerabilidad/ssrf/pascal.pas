{ Server-Side Request Forgery (SSRF) }
program Example;
begin
  Response.Content := TFPHTTPClient.SimpleGet(Request.QueryFields.Values['url']);
  end.
