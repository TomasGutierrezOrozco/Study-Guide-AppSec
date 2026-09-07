FROM php:8.3-apache
RUN apt-get update && apt-get install -y iputils-ping && rm -rf /var/lib/apt/lists/*
WORKDIR /var/www/html
COPY index.php /var/www/html/index.php
EXPOSE 80
