FROM php:8.3-cli
RUN apt-get update && apt-get install -y iputils-ping && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY index.php /app/index.php
EXPOSE 80
CMD ["php", "-S", "0.0.0.0:80", "-t", "/app"]
