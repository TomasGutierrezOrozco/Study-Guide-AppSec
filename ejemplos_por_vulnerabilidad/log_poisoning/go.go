// Log Poisoning (LFI a RCE)
package main
func demo() {
  os.WriteFile("access.log",[]byte(r.UserAgent()),0644)
  os.ReadFile(r.URL.Query().Get("page"))
  }
