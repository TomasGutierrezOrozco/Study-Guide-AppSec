package main

import (
	"io"
	"net/http"
)

func fetch(w http.ResponseWriter, r *http.Request) {
	u := r.URL.Query().Get("u")
	resp, _ := http.Get(u)
	defer resp.Body.Close()
	io.Copy(w, resp.Body)
}
