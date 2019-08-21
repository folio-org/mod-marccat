FROM folioci/openjdk8-jre:latest

ENV VERTICLE_FILE mod-marccat-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

# Copy your fat jar to the container
COPY target/${VERTICLE_FILE} ${VERTICLE_HOME}/${VERTICLE_FILE}

# Expose this port locally in the container.
EXPOSE 8081


FROM postgres

ENV VERTICLE_HOME /usr/verticles \
 DB_USERNAME=folio_admin \
 DB_PASSWORD=folio_admin5757 \
 DB_HOST=127.0.0.1 \
 DB_PORT=5432 \
 DB_DATABASE=okapi_modules

EXPOSE 5432
