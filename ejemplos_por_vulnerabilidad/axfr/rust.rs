// AXFR Full Zone Transfer
fn demo() {
  Command::new("dig").arg("axfr").arg(domain).output()?;
  }
