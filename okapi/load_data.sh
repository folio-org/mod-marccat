#!/usr/bin/env bash

TENANT=${1:-diku}
TOKEN=${1:-diku}

curl -i -w "\n" -X POST -H "Content-type: application/json" \
    -H "X-Okapi-Tenant: ${TENANT}" -H "X-Okapi-Token: $TOKEN" -d @template.json http://localhost:9130/template

curl -i -w "\n" -X POST -H "Content-type: application/json" \
    -H "X-Okapi-Tenant: ${TENANT}" -H "X-Okapi-Token: $TOKEN" -d @record.json http://localhost:9130/bibliographic-record
