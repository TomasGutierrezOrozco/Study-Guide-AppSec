FROM php:8.3-apache
WORKDIR /var/www/html
RUN mkdir -p /var/www/html/logs
COPY index.php /var/www/html/index.php
COPY home.php /var/www/html/home.php
COPY php.ini /usr/local/etc/php/conf.d/study.ini
EXPOSE 80
