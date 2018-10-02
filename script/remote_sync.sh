#!/usr/bin/env bash
########################################################
# Synchronise remote test Folio mod-cataloging
#
# Author : Christian Chiama - christian.chiama@atcult.it
# Version: 1.0
# Date   : 01/10/2018
#########################################################
cd /home/folio/folio/bin/
sh sync_rem_env.sh ./target/mod-cataloging-1.0.jar
