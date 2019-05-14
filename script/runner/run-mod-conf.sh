#!/usr/bin/env bash
########################################################
# Run script passed as first args from runner folder
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.0
# Date   : 05/05/2019
#########################################################

LOCAL_MOD_CONF_FOLDER=../mod-configuration/
MOD_CONF_FOLDER=${1:-../mod-configuration/}
MOD_CONF_FOLDER_TARGET_JAR=mod-configuration-server/target/
MOD_CONF_JAR=mod-configuration-server-fat.jar
MOD_CONF_PORT=${2:-8085}
WITH_MONGO=${3:-embed_postgres=true}

setup(){
if [  -d ../mod-configuration/${MOD_CONF_FOLDER_TARGET_JAR} ]; then
    java -jar ${MOD_CONF_FOLDER}${MOD_CONF_FOLDER_TARGET_JAR}${MOD_CONF_JAR} -Dhttp.port=${MOD_CONF_PORT} ${WITH_MONGO} &
  else
    cd ${LOCAL_MOD_CONF_FOLDER}
    mvn clean install package
    java -jar ${MOD_CONF_FOLDER}${MOD_CONF_FOLDER_TARGET_JAR}${MOD_CONF_JAR} -Dhttp.port=${MOD_CONF_PORT} ${WITH_MONGO} &
fi
}

setup_with_spring(){
  if ! command -v mvn >/dev/null 2>&1; then
    echo 'Error: Apache Maven is not installed, run with java'
    java -jar ${MOD_CONF_FOLDER}${MOD_CONF_FOLDER_TARGET_JAR}${MOD_CONF_JAR} -Dhttp.port=${MOD_CONF_PORT} ${WITH_MONGO} &
  fi
  run
}

run(){
  cd ../mod-configuration && mvn clean install package
  sleep 1
  mvn spring-boot:run -Dhttp.port=${MOD_CONF_PORT} ${WITH_MONGO} &
}

setup
