#!/usr/bin/env bash
########################################################
# Run script passed as first args from runner folder
# e.g: ./runner mod-conf -> will be transform in: ./script/runner/run-mod-conf.sh [args]
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.0
# Date   : 05/05/2019
#########################################################

SCRIPT_FOLDER=${1:-script/runner}
SCRIPT_NAME=${2:-setup}
SCRIPT_ARGS=${3}
sh ${SCRIPT_FOLDER}/run-${SCRIPT_NAME}.sh ${SCRIPT_ARGS}
