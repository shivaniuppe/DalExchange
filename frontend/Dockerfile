# Use a lightweight Node.js image
FROM node:alpine AS builder

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json (or yarn.lock)
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the project (excluding node_modules)
COPY . .

# Overwrite the development .env with the production .env
COPY .env.prod .env

# Build the project
RUN npm run build

# Use a smaller image for serving
FROM nginx:alpine

# Set working directory
WORKDIR /usr/share/nginx/html

# Copy the build folder from the builder stage
COPY --from=builder /app/build .

# Expose port 80
EXPOSE 80

# Configure Nginx to serve the built React app
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
