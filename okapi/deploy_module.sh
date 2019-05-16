#!/usr/bin/env bash
########################################################
# Declare the module to Okapi
# Deploying the module
# Enable the module for our tenant
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.0
# Date   : 10/04/2019
#########################################################

OKAPI_URL=${1:-localhost}
OKAPI_PORT=${2:-9130}
MOD_MARCCAT_JAR=${3:-mod-marccat-1.4-SNAPSHOT}

curl -i -w '\n' -X GET http://${OKAPI_URL}:${OKAPI_PORT}/_/proxy/tenants

#************* Declare the MARCcat module to Okapi as external module *********************

curl -w '\n' -X POST -D -   \
    -H "Content-type: application/json"   \
    -d @../target/ModuleDescriptor.json \
    http://${OKAPI_URL}:${OKAPI_PORT}/_/proxy/modules

curl -i -w '\n' -X GET http://${OKAPI_URL}:${OKAPI_PORT}/_/proxy/modules
curl -i -w '\n' -X GET http://${OKAPI_URL}:${OKAPI_PORT}/_/proxy/modules/${MOD_MARCCAT_JAR}

#*************** Deploying the module *******************

curl -w '\n' -D - -s \
   -X POST \
   -H "Content-type: application/json" \
   -d @../target/DeploymentDescriptor.json  \
   http://${OKAPI_URL}:${OKAPI_PORT}/_/discovery/modules

#**************** Enable the module for our tenant n******************

curl -i -w '\n' \
   -X POST \
   -H "Content-type: application/json" \
   -d @module_id.json \
   http://${OKAPI_URL}:${OKAPI_PORT}/_/proxy/tenants/diku/modules
