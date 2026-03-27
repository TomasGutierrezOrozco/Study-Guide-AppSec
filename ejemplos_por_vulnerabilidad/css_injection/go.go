// CSS Injection (CSSI)
package main
func demo() {
  fmt.Fprintf(w,"<style>%s</style>",r.URL.Query().Get("css"))
  }
