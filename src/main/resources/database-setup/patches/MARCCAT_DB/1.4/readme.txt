**********************************
* OLISUITE PG DB                 *
*                                *
* Ver. 1.4                       *
* Author: mirko.fonzo@atcult.it  *
* Date: 06/05/2020               *
**********************************


This document is for internal use.

DESCRIPTION
Installation script of the database component MARCCAT DB 1.4 to be included in the MARCcat release
which requires this database patch.
This patch is intended to be integrated into mod-marccat installation.
The patch can be be executed outside mod-marccat for testing purpose.

PREREQUIREMENTS
The following database components must be installed:
 - MARCCAT DB 1.3
   
INSTALLATION AND EXECUTION
 - Copy all the file into the same installation directory.
 - Execute a database backup before to start the installation, because the operation
   cannot be undone automatically.
 - With postgres user run sh install-patch.sh to show the usage of the command.
 - The install-patch.sh executes install-patch.sql, which executes automatically the other sql scripts.
   mod-marccat can run install-patch.sh or directly install-patch.sql passing the proper
   parameters.
 - There is a ckeck into this patch to prevent to be installed more times, so error such the
   one below is normal if more installation attempts are made:
   
     psql:patch-check.sql:21: ERROR:  Check MARCCAT DB version: 1.4 found.
     CONTEXT:  PL/pgSQL function inline_code_block line 16 at RAISE


RELEASE NOTES
1. UXPROD-1460 (it also fixes MODCAT-164)
2. MODCAT-160
3. MODCAT-177
4. MODCAT-175
5. UICAT-62