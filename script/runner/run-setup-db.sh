#!/usr/bin/env bash
########################################################
# Run script passed as first args from runner folder
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.0
# Date   : 05/05/2019
#########################################################


host=${1:-localhost}
port=${2:-5432}
admin_user=${3:-$USER}
admin_password=${4:-}

cd database-setup

run-setup-db.sh test3 marccats admin localhost 5432 postgres postgres

cd ..
#!/usr/bin/env bash
