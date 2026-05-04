{ Open Redirect }
program Example;
begin
  Response.Code := 302; Response.Location := Request.QueryFields.Values['next'];
  end.
