{ XPath Injection }
program Example;
begin
  Expr := '//user[name=''' + Request.QueryFields.Values['user'] + ''']';
  end.
