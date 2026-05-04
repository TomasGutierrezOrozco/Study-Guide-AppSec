package main

import (
	"fmt"
	"io"
	"net/http"
	"strconv"
)

func main() {
	go func() {
		mux := http.NewServeMux()
		mux.HandleFunc("/internal", func(w http.ResponseWriter, r *http.Request) {
			fmt.Fprint(w, "go-internal-admin")
		})
		http.ListenAndServe("127.0.0.1:9191", mux)
	}()

	http.HandleFunc("/fetch", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Credentials", "true")
		resp, err := http.Get(r.URL.Query().Get("url"))
		if err != nil {
			http.Error(w, err.Error(), 500)
			return
		}
		defer resp.Body.Close()
		io.Copy(w, resp.Body)
	})

	http.HandleFunc("/api/report", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Access-Control-Allow-Credentials", "true")
		n, _ := strconv.Atoi(r.URL.Query().Get("limit"))
		if n == 0 {
			n = 5000
		}
		fmt.Fprintf(w, "count=%d", n)
	})

	http.ListenAndServe(":8080", nil)
}
