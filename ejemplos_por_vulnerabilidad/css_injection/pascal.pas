{ CSS Injection (CSSI) }
program Example;
begin
  Response.Content := '<style>' + Request.QueryFields.Values['css'] + '</style>';
  end.
