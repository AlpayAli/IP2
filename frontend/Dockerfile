# Gebruik de Node.js base image
FROM node:20 AS builder

# Declare build arguments
ARG VITE_BACKEND_URL
ARG VITE_KEYCLOAK_URL
ARG VITE_KEYCLOAK_REALM
ARG VITE_KEYCLOAK_CLIENT_ID


# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Install dependencies en bouw de app
RUN echo "VITE_BACKEND_URL=$VITE_BACKEND_URL" \
    && VITE_BACKEND_URL=$VITE_BACKEND_URL npm install \
    && VITE_BACKEND_URL=$VITE_BACKEND_URL npm run build


# Standaard Node.js container
FROM node:20

# Stel de werkdirectory in
WORKDIR /app

# Kopieer gebuilde bestanden van de builder stage
COPY --from=builder /app/dist ./dist

# Declare build arguments
ARG VITE_BACKEND_URL
ARG VITE_KEYCLOAK_URL
ARG VITE_KEYCLOAK_REALM
ARG VITE_KEYCLOAK_CLIENT_ID

# Stel de omgevingsvariabelen in
ENV VITE_BACKEND_URL=$VITE_BACKEND_URL
ENV VITE_KEYCLOAK_URL=$VITE_KEYCLOAK_URL
ENV VITE_KEYCLOAK_REALM=$VITE_KEYCLOAK_REALM
ENV VITE_KEYCLOAK_CLIENT_ID=$VITE_KEYCLOAK_CLIENT_ID

# Echo environment variables
RUN echo "VITE_BACKEND_URL=$VITE_BACKEND_URL"
RUN echo "VITE_KEYCLOAK_URL=$VITE_KEYCLOAK_URL"
RUN echo "VITE_KEYCLOAK_REALM=$VITE_KEYCLOAK_REALM"
RUN echo "VITE_KEYCLOAK_CLIENT_ID=$VITE_KEYCLOAK_CLIENT_ID"

# Installeren van http-server (een lichte Node.js server)
RUN npm install -g http-server


# Expose poort 3000 (kan aangepast worden afhankelijk van je Node.js serverinstelling)
EXPOSE 3000

# Start de HTTP server voor de productie-omgeving
CMD ["http-server", "dist", "-p", "3000"]
