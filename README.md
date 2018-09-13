# mod-cataloging
FOLIO metadata management / cataloging module 

# Goal

FOLIO compatible cataloging module.

# Prerequisites

- Java 8 JDK
- Maven 3.3.9
- Postgres 9.6.1 (running and listening on localhost:5432, logged in user must have admin rights)
- Node 6.4.0 (for API linting and documentation generation)
- NPM 3.10.3 (for API linting and documentation generation)

# Preparation

## Git Submodules

There are some common RAML definitions that are shared between FOLIO projects via Git submodules.

To initialise these please run `git submodule init && git submodule update` in the root directory.

If these are not initialised, the module will fail to build correctly, and other operations may also fail.

More information is available on the [developer site](https://dev.folio.org/guides/developer-setup/#update-git-submodules).

## Issues

Issues are managed here https://issues.folio.org/projects/MODCAT/issues
