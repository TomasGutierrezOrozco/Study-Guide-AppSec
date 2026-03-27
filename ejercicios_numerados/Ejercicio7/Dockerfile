FROM golang:1.24-alpine
WORKDIR /app
COPY main.go /app/main.go
RUN go build -o server /app/main.go
EXPOSE 8080
CMD ["/app/server"]
