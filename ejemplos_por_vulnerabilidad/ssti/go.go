// Server-Side Template Injection (SSTI)
package main
func demo() {
  template.New("x").Parse(r.URL.Query().Get("tpl"))
  }
