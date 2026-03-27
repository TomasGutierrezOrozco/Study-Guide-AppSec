# Remote File Inclusion (RFI)
sub demo {
  print HTTP::Tiny->new->get($url)->{content};
  }
