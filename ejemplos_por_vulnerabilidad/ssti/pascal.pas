{ Server-Side Template Injection (SSTI) }
program Example;
begin
  Template := Request.QueryFields.Values['tpl'];
  end.
