# Set base image
FROM nginx:stable-alpine

# Set working directory in the container
WORKDIR /usr/share/nginx/html

# Clear working directory
RUN rm -rf ./*

# Copy packaged flutter app
COPY /build/web .

# Setup nginx
CMD ["nginx", "-g", "daemon off;"]