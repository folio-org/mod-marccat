FROM folioci/openjdk8-jre:latest

ENV VERTICLE_FILE mod-marccat-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

# Copy your fat jar to the container
COPY target/${VERTICLE_FILE} ${VERTICLE_HOME}/${VERTICLE_FILE}

FROM postgres

ENV VERTICLE_HOME /usr/verticles \
 DB_PORT=5432 \
 DB_DATABASE=okapi_modules

EXPOSE 8081
