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
TOKEN=${1:-diku}

curl -D - -w '\n' -H "X-Okapi-Tenant: ${TENANT}" -H "X-Okapi-Token: ${TOKEN}" http://localhost:9130/templates
curl -D - -w '\n' -H "X-Okapi-Tenant: ${TENANT}" -H "X-Okapi-Token: ${TOKEN}" http://localhost:9130/heading-types
curl -D - -w '\n' -H "X-Okapi-Tenant: ${TENANT}" -H "X-Okapi-Token: ${TOKEN}" http://localhost:9130/bibliographic-record

