// CORS Misconfiguration
package main
func demo() {
  w.Header().Set("Access-Control-Allow-Origin","*")
  w.Header().Set("Access-Control-Allow-Credentials","true")
  }
