# Mass Assignment
sub demo {
  $user->{$_} = $params->{$_} for keys %$params;
  }
