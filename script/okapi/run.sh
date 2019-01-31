#!/usr/bin/env bash

# Parameters
OKAPIPORT=9130
OKAPIURL="151.1.163.1:$OKAPIPORT"
CURL="curl -w\n -D - "
TEN="-H X-Okapi-Tenant:tnx"
JSON="-H Content-Type:application/json"
MOD="mod-marccat-1.2.0"

# Check we are in the right directory
DIRPATH=`pwd`
DIR=`basename $DIRPATH`
if [ $DIR = "script" ]
then
  cd ..
fi

DIRPATH=`pwd`
DIR=`basename $DIRPATH`


# Check we have the fat jar
if [ ! -f target/mod-marccat-1.2.0.jar ]
then
  echo No fat jar found, no point in trying to run
  exit 1
fi

# Start Okapi (in dev mode, no database)
OKAPIPATH="../okapi/okapi-core/target/okapi-core-fat.jar"
java -Dport=$OKAPIPORT -jar $OKAPIPATH dev > okapi.log 2>&1 &
PID=$!
echo Started okapi on port $OKAPIPORT. PID=$PID
sleep 1 # give it time to start
echo

echo "Listing the env before starting the module"
$CURL $OKAPIURL/_/env
echo

# Load mod-notify
echo "Loading mod-marccat"
$CURL -X POST \
  -d@descriptors/ModuleDescriptor-template.json 151.1.163.1/_/proxy/modules
echo


echo "Installing it (deploy and enable)"
$CURL -X POST $TEN \
   -d'[ { "id":"mod-pg-embed", "action":"enable" } ] ' \
   $OKAPIURL/_/proxy/tenants/tnx/install?deploy=true
echo
sleep 1

echo "Listing the env after starting the module"
$CURL $OKAPIURL/_/env
echo

echo "Loading the mock module"
$CURL -X POST \
  -d@descriptors/ModuleDescriptor-template.json $OKAPIURL/_/proxy/modules
echo

echo "Installing it (deploy and enable)"
$CURL -X POST $TEN \
   -d'[ { "id":"mod-marccat", "action":"enable" } ] ' \
   $OKAPIURL/_/proxy/tenants/tnx/install?deploy=true
echo


# Let it run
echo
echo "Hit enter to close"
read

# Clean up
echo "Cleaning up: Killing Okapi $PID"
kill $PID

