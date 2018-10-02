#!/usr/bin/env bash
########################################################
# Synchronise remote test Folio mod-cataloging
#
# Author : Mirko Fonzo - mirko.fonzo@atcult.it
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
  DATE=`date`
  scp -P ${SSH_PORT} ${SSH_SRC_FILE} ${SSH_USR}@${SSH_HOST}:${SSH_DEST_FILE}
  EXIT_CODE=$?

  if [ $EXIT_CODE -eq 0 ]
  then
    nohup echo "cd folio/bin ; sh stop_modcat.sh ; sleep ${RESTART_WAIT_SEC} ; sh start_modcat.sh" | ssh -p ${SSH_PORT} ${SSH_USR}@${SSH_HOST} &
  else
    echo "ERROR - cannot upload file ${SSH_SRC_FILE} via ssh to host ${SSH_HOST} [${EXIT_CODE}]"
  fi
}

go(){
  check_env
  rem_sync
}

go
