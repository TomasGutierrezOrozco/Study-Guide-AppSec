FROM debian:stretch-slim AS builder
RUN apt-get update && apt-get install -y gcc make curl
RUN curl -O https://ftp.gnu.org/gnu/bash/bash-4.3.tar.gz && \
    tar zxvf bash-4.3.tar.gz && \
    cd bash-4.3 && \
    ./configure && \
    make && \
    cp bash /tmp/bash_vulnerable

FROM python:3.14-slim
WORKDIR /app
RUN mkdir -p /app/cgi-bin
COPY --from=builder /tmp/bash_vulnerable /bin/bash
COPY cgi-bin/status.sh /app/cgi-bin/status.sh
RUN chmod +x /app/cgi-bin/status.sh
EXPOSE 8000
CMD ["python", "-m", "http.server", "--cgi", "8000"]
