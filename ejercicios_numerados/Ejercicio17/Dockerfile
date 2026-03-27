FROM ubuntu:24.04
RUN apt-get update && apt-get install -y bind9 dnsutils && rm -rf /var/lib/apt/lists/*
COPY named.conf.local /etc/bind/named.conf.local
COPY db.study.local /etc/bind/db.study.local
EXPOSE 53/tcp 53/udp
CMD ["named", "-g", "-c", "/etc/bind/named.conf"]
