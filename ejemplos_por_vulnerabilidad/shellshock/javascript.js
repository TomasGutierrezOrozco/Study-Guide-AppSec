// ShellShock
function demo(req, res) {
  exec('bash -c "echo $HTTP_USER_AGENT"');
  }
