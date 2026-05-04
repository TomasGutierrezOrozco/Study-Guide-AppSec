{ JWT Enumeration and Exploitation }
program Example;
begin
  Payload := DecodeBase64(SplitString(Token, '.')[1]);
  end.
