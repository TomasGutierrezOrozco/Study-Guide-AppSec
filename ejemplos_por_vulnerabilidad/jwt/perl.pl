# JWT Enumeration and Exploitation
sub demo {
  $payload = decode_base64((split /\./, $token)[1]);
  }
