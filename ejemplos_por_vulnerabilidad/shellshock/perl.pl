# ShellShock
sub demo {
  system('bash', '-c', 'echo $HTTP_USER_AGENT');
  }
