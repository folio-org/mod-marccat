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

token=`cat dikuToken`

curl -D - -w '\n' -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $token" http://localhost:9130/owners
curl -D - -w '\n' -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $token" http://localhost:9130/feefines
# /accounts Require add
curl -D - -w '\n' -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $token" http://localhost:9130/accounts
#Depend to accounts
curl -D - -w '\n' -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $token" http://localhost:9130/feefinehistory
curl -D - -w '\n' -H "X-Okapi-Tenant: diku" -H "X-Okapi-Token: $token" http://localhost:9130/chargeitem
