// Client-Side Template Injection (CSTI)
package main
func demo() {
  fmt.Fprintf(w,`<div>{{%s}}</div>`,r.URL.Query().Get("expr"))
  }
