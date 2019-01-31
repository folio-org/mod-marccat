#!/usr/bin/env bash

database_name=${1:-}
marccat_user_name=${2:-marccat}
host=${3:-localhost}
port=${4:-5432}
admin_user=${5:-$USER}
admin_password=${6:-}

create_database_sql=$(cat ./create-db.sql)
create_database_sql="${create_database_sql//database_name/$database_name}"
create_database_sql="${create_database_sql//user_name/$marccat_user_name}"

env PGPASSWORD=${admin_password} psql -h ${host} -p ${port} -U ${admin_user} -w << EOF
${create_database_sql}
EOF
