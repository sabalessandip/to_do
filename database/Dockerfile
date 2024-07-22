# Set base image
FROM mysql:latest

# Copy sql file to required directory
COPY create_database.sql /docker-entrypoint-initdb.d/

# Accept the root password for mysql
ARG MYSQL_ROOT_PASSWORD

# Set the root password to mysql environment variable
ENV MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}