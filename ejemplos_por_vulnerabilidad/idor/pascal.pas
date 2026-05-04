{ IDOR }
program Example;
begin
  Response.Content := GetInvoice(Request.QueryFields.Values['id']);
  end.
