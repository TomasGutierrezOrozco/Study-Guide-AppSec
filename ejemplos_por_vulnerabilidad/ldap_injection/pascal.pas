{ LDAP Injection }
program Example;
begin
  Filter := '(uid=' + Request.QueryFields.Values['user'] + ')';
  end.
