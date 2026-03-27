{ Client-Side Template Injection (CSTI) }
program Example;
begin
  Response.Content := '<div>{{' + Request.QueryFields.Values['expr'] + '}}</div>';
  end.
