# Set base image
FROM nginx:alpine

# Copy the NGINX configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 80
EXPOSE 80

# Start NGINX when the container has started
CMD ["nginx", "-g", "daemon off;"]
