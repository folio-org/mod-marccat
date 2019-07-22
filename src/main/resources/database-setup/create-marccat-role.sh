#!/usr/bin/env bash

marccat_user_name=${1:-marccat}
marccat_password=${2:-admin}
host=${3:-localhost}
port=${4:-5432}
admin_user=${5:-$USER}
admin_password=${6:-}

create_role_sql=$(cat ./create-marccat-role.sql)
create_role_sql="${create_role_sql//user_name/$marccat_user_name}"
create_role_sql="${create_role_sql//password/$marccat_password}"

env PGPASSWORD=${admin_password} psql -h ${host} -p ${port} -U ${admin_user} -w << EOF
${create_role_sql}
EOF
