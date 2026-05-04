{ ShellShock }
program Example;
begin
  RunCommand('/bin/bash', ['-c', 'echo $HTTP_USER_AGENT'], Output);
  end.
