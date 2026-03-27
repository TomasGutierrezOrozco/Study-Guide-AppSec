package main

import (
	"fmt"
	"io"
	"net/http"
	"os"
)

func main() {
	go func() {
		mux := http.NewServeMux()
		mux.HandleFunc("/admin", func(w http.ResponseWriter, r *http.Request) {
			fmt.Fprint(w, "internal-admin-panel")
		})
		http.ListenAndServe("127.0.0.1:9090", mux)
	}()

	_ = os.MkdirAll("/data", 0755)
	_ = os.WriteFile("/data/readme.txt", []byte("public file"), 0644)
	_ = os.WriteFile("/secret.txt", []byte("top-secret"), 0644)

	http.HandleFunc("/fetch", func(w http.ResponseWriter, r *http.Request) {
		target := r.URL.Query().Get("url")
		resp, err := http.Get(target)
		if err != nil {
			http.Error(w, err.Error(), 500)
			return
		}
		defer resp.Body.Close()
		io.Copy(w, resp.Body)
	})

	http.HandleFunc("/file", func(w http.ResponseWriter, r *http.Request) {
		name := r.URL.Query().Get("name")
		data, err := os.ReadFile("/data/" + name)
		if err != nil {
			http.Error(w, err.Error(), 500)
			return
		}
		w.Write(data)
	})

	http.ListenAndServe(":8080", nil)
}
