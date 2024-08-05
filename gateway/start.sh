#!/bin/sh

# Replace environment variables in nginx configuration
envsubst '\$BACKEND_URL' < /etc/nginx/nginx.template.conf > /etc/nginx/nginx.conf

# Start nginx
nginx -g 'daemon off;'
