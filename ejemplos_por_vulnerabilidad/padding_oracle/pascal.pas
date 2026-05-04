{ Padding Oracle }
program Example;
begin
  try Decrypt(Token); except on E: Exception do Response.Code := 403; end;
  end.
