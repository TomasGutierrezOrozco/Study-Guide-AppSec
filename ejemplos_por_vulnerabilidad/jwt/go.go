// JWT Enumeration and Exploitation
package main
func demo() {
  payload,_:=base64.RawURLEncoding.DecodeString(strings.Split(token,".")[1])
  _ = payload
  }
