FROM ubuntu:24.04
RUN apt-get update && apt-get install -y squid && rm -rf /var/lib/apt/lists/*
COPY squid.conf /etc/squid/squid.conf
EXPOSE 3128
CMD ["squid", "-N"]
