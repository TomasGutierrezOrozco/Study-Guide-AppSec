{ WebDAV Enumeration and Exploitation }
program Example;
begin
  if Request.Method = 'PUT' then SaveFile(Request.PathInfo, Request.Content);
  end.
