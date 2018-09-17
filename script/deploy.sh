#!/usr/bin/env bash
#author christian chiama
echo "kill previous deploy....."
PORT_NUMBER=8080
lsof -i tcp:${PORT_NUMBER} | awk 'NR!=1 {print $2}' | xargs kill
echo "killing........."
sleep 3
echo "Deploy new artifact........."
ohup java -Dserver.port=8889 -jar ./target/mod-cataloging-1.0.jar &
