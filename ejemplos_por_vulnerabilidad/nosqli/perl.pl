# NoSQL Injection
sub demo {
  $collection->find_one(decode_json($body));
  }
