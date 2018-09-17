#!/usr/bin/env bash
echo "cleaning....."
PID=$(lsof -t -i:8889)
kill -9 $PID
sleep 3
