{ Cross-Site Request Forgery (CSRF) }
program Example;
begin
  if Request.Method = 'POST' then ChangeEmail(Request.ContentFields.Values['email']);
  end.
