{ Log Poisoning (LFI a RCE) }
program Example;
begin
  WriteLn(LogFile, Request.UserAgent);
  Response.Content := LoadFile(Request.QueryFields.Values['page']);
  end.
