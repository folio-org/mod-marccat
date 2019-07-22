#!/usr/bin/env bash

database_name=${1:-}
marccat_user_name=${2:-marccat}

usage(){
  echo ""
  echo " Usage: $0 {database_name} [marccat_user_name]"
  echo ""
  echo " Examples: ./create-objects biblio marccat"
  echo "           ./create-objects biblio"
  echo ""
  echo "           DEFAULT VALUES"
  echo "           marccat_user_name: marccat"
  echo ""
}

param_check(){
  if [ "$database_name" = "" ]
  then
    usage
    exit 1
  fi
}

setup(){
  if ! command -v psql >/dev/null 2>&1; then
    echo 'Error: psql is not installed.' >&2
    exit 1
  fi

  param_check

  psql -v user_name=${marccat_user_name} -f create-objects.sql ${database_name}
}

setup
