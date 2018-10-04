#!/usr/bin/env bash
########################################################
# deploy Folio mod-cataloging
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.0
# Date   : 01/19/2018
#########################################################
echo "kill previous deploy....."
PORT_NUMBER=8080
lsof -i tcp:${PORT_NUMBER} | awk 'NR!=1 {print $2}' | xargs kill
echo "killing........."
sleep 3
echo "Deploy new artifact........."
nohup java -Dserver.port=8080 -jar ./target/mod-cataloging-1.0.jar &
