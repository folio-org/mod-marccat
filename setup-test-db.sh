#!/usr/bin/env bash

host=${1:-localhost}
port=${2:-5432}
admin_user=${3:-$USER}
admin_password=${4:-}

cd database-setup

./setup-db.sh test3 marccats admin localhost 5432 postgres postgres

cd ..
