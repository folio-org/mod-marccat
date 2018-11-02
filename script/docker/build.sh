#!/usr/bin/env bash

mvn clean compile install -DskipTests=true

docker build -t mod-marccat .
