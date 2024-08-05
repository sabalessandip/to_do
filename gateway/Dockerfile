# Set base image
FROM nginx:alpine

# Copy nginx template configuration file
COPY nginx.template.conf /etc/nginx/nginx.template.conf

# Copy the start script
COPY start.sh /start.sh

# Ensure the start script is executable
RUN chmod +x /start.sh

# Set the entrypoint to the start script
ENTRYPOINT ["/start.sh"]
