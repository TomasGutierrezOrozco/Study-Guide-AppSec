{ API Abuse }
program Example;
begin
  Limit := StrToIntDef(Request.QueryFields.Values['limit'], 1000000);
  end.
