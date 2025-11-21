FROM node:20-alpine AS build

WORKDIR /app

# Copy package files first to leverage docker cache for dependencies
COPY package.json package-lock.json* ./

# Try `npm ci` (requires package-lock.json). If it fails (no lockfile), fallback to `npm install`.
RUN npm ci --production=false --silent || npm install --production=false --silent

# Copy sources and build
COPY . .
RUN npm run build

# Serve with nginx (lightweight alpine)
FROM nginx:stable-alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
