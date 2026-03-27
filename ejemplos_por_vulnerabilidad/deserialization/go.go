// Insecure Deserialization
package main
func demo() {
  gob.NewDecoder(r.Body).Decode(&obj)
  }
