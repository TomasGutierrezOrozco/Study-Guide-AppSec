// LDAP Injection
package main
func demo() {
  filter:="(uid="+r.URL.Query().Get("user")+")"
  }
