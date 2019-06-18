# mod-marccat

Copyright (C) 2018-2019 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

* [Introduction](#introduction)
* [Compilation](#compilation)
* [Docker Build](#docker-build)
* [Initialize Postgres Database](#initialize-postgres-database)
* [Installing MARCcat module](#installing-marccat-module)
* [Deploying MARCcat module](#deploying-marccat-module)

## Introduction

FOLIO Cataloguing module.

## Compilation

```
   mvn clean install
```

See that it says "BUILD SUCCESS" near the end.

## Docker Build

Build the docker container running from root folder:

```
   docker build -t mod-marccat .
```

Actually there are no test, but in near future runs with it:

```
   docker run -t -i -p 8081:8081 mod-marccat
```

## Installing MARCcat module

Follow the guide of
[Deploying Modules](https://github.com/folio-org/okapi/blob/master/doc/guide.md#example-1-deploying-and-using-a-simple-module)
sections of the Okapi Guide and Reference, which describe the process in detail.

First of all you need a running Okapi instance.
(Note that [specifying](../README.md#setting-things-up) an explicit 'okapiurl' might be needed.)

```
   cd ../okapi
   java -jar okapi-core/target/okapi-core-fat.jar dev [for development mode]
```

Declare MARCcat module to Okapi:

```
curl -w '\n' -X POST -D -   \
   -H "Content-type: application/json"   \
   -d @target/ModuleDescriptor.json \
   http://localhost:9130/_/proxy/modules
```

That ModuleDescriptor tells Okapi what the module is called, what services it
provides, and how to deploy it.

## Deploying MARCcat module

Next we need to deploy the module. There is a deployment descriptor in
`target/DeploymentDescriptor.json`. It tells Okapi to start the module on 'localhost'.

Deploy it via Okapi discovery:

```
curl -w '\n' -D - -s \
  -X POST \
  -H "Content-type: application/json" \
  -d @target/DeploymentDescriptor.json  \
  http://localhost:9130/_/discovery/modules
```

Then we need to enable the module for the tenant:

```
curl -w '\n' -X POST -D -   \
    -H "Content-type: application/json"   \
    -d @target/TenantModuleDescriptor.json \
    http://localhost:9130/_/proxy/tenants/<tenant_name>/modules
```
## Maximum upload file size and java heap memory setups
mod-data-import provides the ability to uplaod a file of any size. The only limitation is related to the current implementation of the RMB and the size of the heap in the java process. Currently, before saving the file, it is read into memory, respectively, it is necessary to have the size of the java heap equal to the file size plus 10 percent.

### Example
| File Size | Java Heap size |
|:---------:|:--------------:|
|   256mb   |     270+ mb    |
|   512mb   |     560+ mb    |
|    1GB    |     1.1+ GB    |

## Setup Mod-configuration
MARCcat needed some configuration to setup and confugure all table and relation of db. After installing all you needed is run:

```
   sh setup/setup-conf.sh [options]
```
where options are:
* host;
* port;
* database parameter;
  * name;
  * username;
  * passord;


## Storage Architecture
mod-marccat has its own pre-existing storage, it results in a single cohesive module, where the data and logic tiers are logically divided within the same artifact.
It uses Hibernate for dealing with the persistence logic.
Tenants are isolated at database level, so multi-tenancy is managed by mod-marccat addressing all the requests from different tenants to the proper database.
                                                     
## Initialize Postgres Database        

to configure postgres database run:

```
  sh setup-test-db [database_name] [marccat_user_name] [marccat_password] [host] [port] [admin_user] [admin_password
```
where options are:
* database (required);
* username;à
* password;
* host;
* port;
* admin user;
* admin password;
for instance:

```
  sh setup-test-db biblio marccat admin localhost 5432 postgres postgres
```


## Issue tracker

See project [MODCAT](https://issues.folio.org/browse/MODCAT)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker/).

## Additional information

The [raml-module-builder](https://github.com/folio-org/raml-module-builder) framework.

Other [modules](https://dev.folio.org/source-code/#server-side).

See project [MODDATAIMP](https://issues.folio.org/browse/MODDATAIMP) at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

Other FOLIO Developer documentation is at [dev.folio.org](https://dev.folio.org/)
