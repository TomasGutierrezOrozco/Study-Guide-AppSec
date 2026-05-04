{ AXFR Full Zone Transfer }
program Example;
begin
  RunCommand('dig', ['axfr', Request.QueryFields.Values['domain']], Output);
  end.
