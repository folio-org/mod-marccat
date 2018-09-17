#!/usr/bin/env bash
echo "kill previous deploy....."
PORT_NUMBER=8889
lsof -i tcp:${PORT_NUMBER} | awk 'NR!=1 {print $2}' | xargs kill
sleep 2
