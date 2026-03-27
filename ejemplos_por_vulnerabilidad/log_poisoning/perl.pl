# Log Poisoning (LFI a RCE)
sub demo {
  print $log $ENV{'HTTP_USER_AGENT'};
  }
