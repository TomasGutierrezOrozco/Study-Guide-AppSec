{ SQL Injection (SQLI) }
program Example;
begin
  Query.SQL.Text := 'SELECT * FROM users WHERE id = ' + ParamStr(1);
  end.
