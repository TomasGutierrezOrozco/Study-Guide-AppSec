{ Race Condition }
program Example;
begin
  if not Coupon.Used then begin Credit(User); Coupon.Used := True; end;
  end.
