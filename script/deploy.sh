#!/usr/bin/env bash
#author christian chiama

echo "kill process and start deploy....."
nohup java -Dserver.port=8888 -jar ./target/mod-cataloging-1.0.jar
