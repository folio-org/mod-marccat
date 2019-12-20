#!/usr/bin/env bash

database_name=${1:-}
marccat_user_name=${2:-marccat}

usage(){
  echo ""
  echo " Usage: $0 {database_name} [marccat_user_name]"
  echo ""
  echo " Examples: ./install-patch.sh biblio marccat"
  echo "           ./install-patch.sh biblio"
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
  . ./env.conf
  echo patch_file=${patch_file}

  psql -v user_name=${marccat_user_name} -v patch_rel_nbr=${patch_rel_nbr} -v patch_sp_nbr=${patch_sp_nbr}  -v patch_comp_typ=${patch_comp_typ} -f ${patch_file} ${database_name}
}

setup
