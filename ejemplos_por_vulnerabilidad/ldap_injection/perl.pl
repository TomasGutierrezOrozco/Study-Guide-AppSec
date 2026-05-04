# LDAP Injection
sub demo {
  $filter = '(uid=' . param('user') . ')';
  }
