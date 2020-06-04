#!/usr/bin/env bash

database_suffix="_marccat"
mod_configuration_host=${1:-localhost}
mod_configuration_port=${2:-}
tenant=${3:-}
database_host=${4:-localhost}
database_port=${5:-5432}
database_name=${6:-${tenant}${database_suffix=}}
database_user=${7:-$USER}
database_password=${8:-}
database_conn_string=jdbc:postgresql://${database_host}:${database_port}/${database_name}


curl -X POST "http://${mod_configuration_host}:${mod_configuration_port}/configurations/entries/" -H "x-okapi-tenant: ${tenant}" -H "Content-Type: application/json" -d '{"module": "MARCCAT", "configName": "datasource", "code": "url", "description": "Target JDBC URL", "default": true, "enabled": true, "value": "'"${database_conn_string}"'" }'
curl -X POST "http://${mod_configuration_host}:${mod_configuration_port}/configurations/entries/" -H "x-okapi-tenant: ${tenant}" -H "Content-Type: application/json" -d '{"module": "MARCCAT", "configName": "datasource", "code": "user", "description": "Database username", "default": true, "enabled": true, "value": "'"${database_user}"'"}'
curl -X POST "http://${mod_configuration_host}:${mod_configuration_port}/configurations/entries/" -H "x-okapi-tenant: ${tenant}" -H "Content-Type: application/json" -d '{"module": "MARCCAT", "configName": "datasource", "code": "password", "description": "Database password", "default": true, "enabled": true, "value": "'"${database_password}"'"}'
