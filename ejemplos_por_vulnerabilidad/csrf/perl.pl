# Cross-Site Request Forgery (CSRF)
sub demo {
  change_email(param('email')) if request_method() eq 'POST';
  }
