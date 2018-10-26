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
SSH_HOST_DEMO=151.1.163.20
SSH_PORT=22022
SSH_USR=folio
SSH_USR_DEMO=root
SSH_DEST_FILE=/usr/local/folio/data
RESTART_WAIT_SEC=3


ssh_itnet_deploy(){
  echo "uploading artifact via ssh on ITNET"
  scp -P ${SSH_PORT} ${SSH_SRC_FILE} folio@${SSH_HOST}:${SSH_DEST_FILE}
  sleep ${RESTART_WAIT_SEC}
  echo "uploaded artifact succesfully. Deploy last release of modcat"
  nohup echo "cd /usr/local/folio/bin ; sh deploy_mod-cat.sh" | ssh -p ${SSH_PORT} folio@${SSH_HOST} &
}

ssh_itnet_deploy_demo(){
  echo "try deploy......."
  echo "uploading artifact via ssh on ITNET [DEMO]"
  scp -P ${SSH_PORT} ${SSH_SRC_FILE} root@${SSH_HOST_DEMO}:${SSH_DEST_FILE}
  sleep ${RESTART_WAIT_SEC}
  nohup java -jar ${SSH_SRC_FILE} | ssh -p ${SSH_PORT} root@${SSH_HOST_DEMO} &
  echo "uploaded artifact succesfully. Deploy last demo release of modcat"
}
ssh_itnet_deploy
echo "deploy on ${SSH_HOST} on $(date) succefully "| mail -s "backup complete" christian.chiama@atcult.it
