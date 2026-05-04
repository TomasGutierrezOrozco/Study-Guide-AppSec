# Padding Oracle
sub demo {
  eval { decrypt($token) }; if ($@) { print 'bad padding' }
  }
