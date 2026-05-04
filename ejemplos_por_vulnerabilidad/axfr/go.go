// AXFR Full Zone Transfer
package main
func demo() {
  exec.Command("dig","axfr",r.URL.Query().Get("domain")).Output()
  }
