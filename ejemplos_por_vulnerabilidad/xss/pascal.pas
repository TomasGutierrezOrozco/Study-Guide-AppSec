{ Cross-Site Scripting (XSS) }
program Example;
begin
  Response.Content := '<h1>' + Request.QueryFields.Values['q'] + '</h1>';
  end.
