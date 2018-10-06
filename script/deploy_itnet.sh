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
SSH_DEST_FILE=/usr/local/folio/data
RESTART_WAIT_SEC=3


ssh_itnet_deploy(){
  echo "uploading artifact via ssh on ITNET"
  scp -P ${SSH_PORT} ${SSH_SRC_FILE} folio@${SSH_HOST}:${SSH_DEST_FILE}
  sleep ${RESTART_WAIT_SEC}
  echo "uploaded artifact succesfully. Deploy last release of modcat"
  nohup echo "cd /usr/local/folio/bin ; sh deploy_mod-cat.sh" | ssh -p ${SSH_PORT} folio@${SSH_HOST} &
}
ssh_itnet_deploy
