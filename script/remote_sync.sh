#!/usr/bin/env bash
########################################################
# Synchronise remote test Folio mod-cataloging
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.0
# Date   : 01/10/2018
#########################################################

SSH_SRC_FILE=./target/mod-cataloging-1.0.jar
SSH_HOST=151.1.165.20
SSH_PORT=22022
SSH_USR=folio
SSH_DEST_FILE=/tmp/
RESTART_WAIT_SEC=3


rem_sync(){
scp -P 22022 ${SSH_SRC_FILE} folio@151.1.165.20:/usr/local/folio/data
echo "cd /usr/local/folio/bin ; sh deploy_mod-cat.sh" | ssh -p 22022 folio@151.1.165.20
}
rem_sync
