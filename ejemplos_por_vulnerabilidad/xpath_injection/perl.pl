# XPath Injection
sub demo {
  $expr = "//user[name='" . param('user') . "']";
  }
