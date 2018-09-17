#!/usr/bin/env bash
echo "cleaning....."
PORT_NUMBER=8889
lsof -i tcp:${PORT_NUMBER} | awk 'NR!=1 {print $2}' | xargs kill
