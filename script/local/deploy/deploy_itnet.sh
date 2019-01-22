#!/usr/bin/env bash
########################################################
# Synchronise remote test Folio mod-cataloging
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.2
# Date   : 01/10/2018
#########################################################

SSH_SRC_FILE=./target/mod-marccat-1.2.0.jar
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
  nohup echo "cd /usr/local/folio/bin ; sh stop_modcat.sh; cd ../data/; java -jar mod-marccat-1.2.0.jar &" | ssh -p ${SSH_PORT} folio@${SSH_HOST} &
}
ssh_itnet_deploy

