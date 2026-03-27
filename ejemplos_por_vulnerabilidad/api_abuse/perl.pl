# API Abuse
sub demo {
  @items = (1..(param('limit') || 1000000));
  }
