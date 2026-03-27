// ShellShock
fn demo() {
  Command::new("bash").arg("-c").arg("echo $HTTP_USER_AGENT").output()?;
  }
