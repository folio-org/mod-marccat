#!/usr/bin/env bash
kill -9 `lsof -t -i :8080`
sleep 2
nohup java -Dserver.port=8888 -jar ./target/mod-cataloging-1.0.jar
