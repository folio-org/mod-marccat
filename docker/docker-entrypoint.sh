#!/usr/bin/env bash

# This is the Docker entrypoint script specified in
# Dockerfile.   It supports invoking the container
# with both optional JVM runtime options as well as
# optional module arguments.
#
# Example:
#
#  'docker run -d -e JAVA_OPTS="-Xmx256M" folio-module embed_mongo=true'
#
set -e
docker exec -it ${VERTICLE_HOME}/${VERTICLE_FILE} psql -U postgres -c "CREATE DATABASE folio_marccat GRANT ALL PRIVILEGES ON DATABASE postgres TO folio_marccat;"

if [ -n "$JAVA_OPTS" ]; then
   exec java "$JAVA_OPTS" -Dserver.port=8081 -jar ${VERTICLE_HOME}/module.jar "$@"
else
   exec java -Dserver.port=8081 -jar ${VERTICLE_HOME}/module.jar "$@"
fi
