// Log Poisoning (LFI a RCE)
fn demo() {
  std::fs::write("access.log", ua)?;
  }
