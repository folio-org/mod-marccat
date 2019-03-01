#!/usr/bin/env bash
########################################################
# Synchronise remote test Folio mod-cataloging
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.2
# Date   : 01/10/2018
#########################################################

SSH_SRC_FILE=./target/mod-marccat-1.3.0.jar
SSH_HOST=151.1.165.20
SSH_PORT=22022
SSH_USR=root
SSH_DEST_FILE=/usr/local/folio/env1/1.2/lib
RESTART_WAIT_SEC=3
SSH_BIN_FOLDER=/usr/local/folio/env1/1.2/bin


ssh_atcult_deploy(){
  echo "uploading artifact via ssh on ITNET"
  scp -P ${SSH_PORT} ${SSH_SRC_FILE} root@${SSH_HOST}:${SSH_DEST_FILE}
  sleep ${RESTART_WAIT_SEC}
  echo "uploaded artifact succesfully. Deploy last release of modcat"
  cd ${SSH_BIN_FOLDER}
  sh stop_modmarccat.sh
  sleep ${RESTART_WAIT_SEC}
  nohup echo "sh ${SSH_BIN_FOLDER}/start_modmarccat.sh" | ssh -p ${SSH_PORT} root@${SSH_HOST} &
}
ssh_atcult_deploy
