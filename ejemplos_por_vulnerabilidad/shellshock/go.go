// ShellShock
package main
func demo() {
  exec.Command("bash","-c","echo $HTTP_USER_AGENT").Run()
  }
