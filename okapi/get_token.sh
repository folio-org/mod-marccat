#!/usr/bin/env bash
########################################################
# Declare the module to Okapi
# Deploying the module
# Enable the module for our tenant:
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.0
# Date   : 10/04/2019
#########################################################

TENANT=${1:-diku}


curl -w '\n' -X POST -D - \
  -H "Content-type: application/json" \
  -H "X-Okapi-Tenant: ${TENANT}" \
  -d @ologin.json \
  http://localhost:9130/authn/login
