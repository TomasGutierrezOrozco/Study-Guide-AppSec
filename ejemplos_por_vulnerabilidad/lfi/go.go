// Local File Inclusion (LFI)
package main
func demo() {
  os.ReadFile(r.URL.Query().Get("file"))
  }
