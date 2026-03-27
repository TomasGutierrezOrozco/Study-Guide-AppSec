# AXFR Full Zone Transfer
sub demo {
  system('dig', 'axfr', param('domain'));
  }
