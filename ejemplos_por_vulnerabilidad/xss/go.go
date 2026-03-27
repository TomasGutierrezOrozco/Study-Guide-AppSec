// Cross-Site Scripting (XSS)
package main
func demo() {
  fmt.Fprintf(w,"<h1>%s</h1>",r.URL.Query().Get("q"))
  }
