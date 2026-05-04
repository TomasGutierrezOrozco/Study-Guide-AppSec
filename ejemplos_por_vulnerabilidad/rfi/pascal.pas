{ Remote File Inclusion (RFI) }
program Example;
begin
  Response.Content := TFPHTTPClient.SimpleGet(Request.QueryFields.Values['url']);
  end.
