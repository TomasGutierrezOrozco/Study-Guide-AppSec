# Server-Side Request Forgery (SSRF)
sub demo {
  print HTTP::Tiny->new->get(param('url'))->{content};
  }
