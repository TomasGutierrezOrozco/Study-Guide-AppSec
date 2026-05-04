{ CORS Misconfiguration }
program Example;
begin
  Response.SetCustomHeader('Access-Control-Allow-Origin', '*');
  end.
