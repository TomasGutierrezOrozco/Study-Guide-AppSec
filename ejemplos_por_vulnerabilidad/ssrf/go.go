// Server-Side Request Forgery (SSRF)
package main
func demo() {
  http.Get(r.URL.Query().Get("url"))
  }
