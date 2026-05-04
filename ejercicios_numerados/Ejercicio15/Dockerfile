FROM python:3.14-slim
WORKDIR /app
COPY server.py /app/server.py
RUN mkdir -p /dav && echo "hello" > /dav/index.txt
EXPOSE 8080
CMD ["python", "/app/server.py"]
