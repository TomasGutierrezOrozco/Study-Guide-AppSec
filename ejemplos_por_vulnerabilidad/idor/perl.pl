# IDOR
sub demo {
  print encode_json(get_invoice(param('id')));
  }
