FROM python:3.14-slim
WORKDIR /app
RUN mkdir -p /app/cgi-bin
COPY cgi-bin/status.sh /app/cgi-bin/status.sh
RUN chmod +x /app/cgi-bin/status.sh
EXPOSE 8000
CMD ["python", "-m", "http.server", "--cgi", "8000"]
