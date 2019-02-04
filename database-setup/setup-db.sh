#!/usr/bin/env bash

database_name=${1:-}
marccat_user_name=${2:-marccat}
marccat_password=${3:-admin}
host=${4:-localhost}
port=${5:-5432}
admin_user=${6:-$USER}
admin_password=${7:-}

usage(){
  echo ""
  echo " Usage: $0 {database_name} [marccat_user_name] [marccat_password] [host] [port] [admin_user] [admin_password]"
  echo ""
  echo " Examples: ./setup-db.sh biblio marccat marccat123 localhost 5432 postgres postgres"
  echo "           ./setup-db.sh biblio marccat \"\" \"\" 5433"
  echo "           ./setup-db.sh biblio"
  echo ""
  echo "           DEFAULT VALUES"
  echo "           marccat_user_name: marccat"
  echo "           marccat_password : admin"
  echo "           host             : localhost"
  echo "           port             : 5432"
  echo "           admin_user       : current user, normally postgres"
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

  ./create-marccat-role.sh ${marccat_user_name} ${marccat_password} ${host} ${port} ${admin_user} ${admin_password}
  ./create-db.sh ${database_name} ${marccat_user_name} ${host} ${port} ${admin_user} ${admin_password}
}

setup
