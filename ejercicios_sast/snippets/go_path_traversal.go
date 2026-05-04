package main

import (
	"net/http"
	"os"
)

func file(w http.ResponseWriter, r *http.Request) {
	name := r.URL.Query().Get("name")
	data, _ := os.ReadFile("/data/" + name)
	w.Write(data)
}
