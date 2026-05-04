// Open Redirect
package main
func demo() {
  http.Redirect(w,r,r.URL.Query().Get("next"),302)
  }
