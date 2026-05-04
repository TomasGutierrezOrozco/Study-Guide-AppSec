// IDOR
package main
func demo() {
  db.QueryRow("SELECT * FROM invoices WHERE id=?",r.URL.Query().Get("id"))
  }
