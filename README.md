# mod-marccat

Copyright (C) 2017-2018 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

## Introduction

Metadata management / foolio marccat module.

# Prerequisites

* Java 8 JDK
* Maven 3.3.9
* Postgres 9.6.1 (running and listening on localhost:5432, logged in user must have admin rights)

# Preparation

## Git Submodules

There are some common RAML definitions that are shared between FOLIO projects via Git submodules.

To initialise these please run `git submodule init && git submodule update` in the root directory.

If these are not initialised, the module will fail to build correctly, and other operations may also fail.

More information is available on the [developer site](https://dev.folio.org/guides/developer-setup/#update-git-submodules).

## Additional information

### Issue tracker

See project [MODCAT](https://issues.folio.org/browse/MODCAT)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

Other FOLIO Developer documentation is at [dev.folio.org](https://dev.folio.org/)
