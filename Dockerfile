FROM folioci/openjdk8-jre-alpine:latest

ENV VERTICLE_FILE mod-marccat-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME /usr/verticles

# Copy your fat jar to the container
COPY target/${VERTICLE_FILE} ${VERTICLE_HOME}/${VERTICLE_FILE}

# Expose this port locally in the container.
EXPOSE 8081


FROM postgres
ENV POSTGRES_DB ${POSTGRES_DB}
EXPOSE 5432
