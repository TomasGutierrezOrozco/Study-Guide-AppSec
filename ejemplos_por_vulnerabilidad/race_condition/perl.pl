# Race Condition
sub demo {
  unless ($coupon->{used}) { credit($user); $coupon->{used}=1; }
  }
