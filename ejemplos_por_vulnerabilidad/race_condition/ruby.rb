# Race Condition
def demo(params)
  unless coupon.used
    credit(user)
    coupon.update!(used: true)
  end
  end
