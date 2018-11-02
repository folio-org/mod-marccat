#!/usr/bin/env bash

MODULEDESC=${1:-"./descriptors/ModuleDescriptor.json"}
OKAPI=${2:-"http://localhost:9130"}
TENANT=${3:-"tnx"}

echo "Check if Okapi is contactable"
curl -w '\n' -X GET -D -   \
     "http://localhost:9130/_/env" || exit 1

sh ./script/okapi/tenant.sh

if [ ! -f $MODULEDESC ]
then
  echo "No Module Descriptor found in '$MODULEDESC'"
  exit 1
fi

MODID=`grep '"id"' ./descriptors/ModuleDescriptor.json  | head -1 | cut -d '"' -f4`

# Create the Docker image
if [ -f Dockerfile ]
then
  docker build -t $MODID .
fi

# Load ModuleDescriptor to Okapi
echo
echo "Declaring module $MODID..."
curl  -D - -w '\n' \
  -X POST \
  -H "Content-type: application/json"  \
  -d @$MODULEDESC \
  $OKAPI/_/proxy/modules


echo "Deploying the module..."
DEPL=/tmp/module-load-deploy-$MODID
cat >$DEPL <<END
{
  "srvcId" : "$MODID",
  "nodeId" : "localhost"
}
END
curl -w '\n' -D - -s \
  -X POST \
  -H "Content-type: application/json" \
  -d ./descriptors/DeploymentDescriptor-template.json \
  http://localhost:9130/_/discovery/modules

curl  -D - -w '\n' \
  -X POST \
  -H "Content-type: application/json"  \
  -d @$DEPL \
  $OKAPI/_/discovery/modules

rm $DEPL

# Check that we have the tenant defined
curl -w '\n' http://localhost:9130/_/tenants/$TENANT | grep $TENANT || (
  echo
  echo "No tenant $TENANT found, creating one"
  TEN=/tmp/module-load-tenant-$TENANTID
  cat >$TEN <<END
    {
      "id" : "$TENANT",
      "name" : "Test Library",
      "description" : "Test library called $TENANT"
    }
END

  curl  -D - -w '\n' \
    -X POST \
    -H "Content-type: application/json"  \
    -d @$TEN \
    $OKAPI/_/proxy/tenants

  echo Created $TENANT

)


# Enable the module for the tenant
echo
echo "Enabling the module '$MODID' for tenant '$TENANT'"
ENAB=/tmp/module-load-enable-$MODID
cat >$ENAB <<END
{
  "id" : "$MODID"
}
END
curl  -D - -w '\n' \
  -X POST \
  -H "Content-type: application/json"  \
  -d @$ENAB \
  $OKAPI/_/proxy/tenants/$TENANT/modules
rm $ENAB


