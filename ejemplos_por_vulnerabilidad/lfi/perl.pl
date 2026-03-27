# Local File Inclusion (LFI)
sub demo {
  print do { local(@ARGV, $/) = $file; <> };
  }
