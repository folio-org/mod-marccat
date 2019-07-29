\echo - MARCCAT DB
\echo - Ver. 1.2
\echo - @Cult s.r.l.
\echo - Date: 26/07/2019
\echo - PostgreSQL compatibility: 9.6+
\echo
\echo MARCCAT DB 1.2 - Start -------------------------------------------------------------
\echo
set myvars.patch_comp_typ TO :'patch_comp_typ';
set myvars.patch_sp_nbr TO :'patch_sp_nbr';
set myvars.patch_rel_nbr TO :'patch_rel_nbr';
\set ON_ERROR_STOP on
\ir patch-check.sql
\set ON_ERROR_STOP off
\ir init_template.sql
\ir MODCAT-91.sql
\ir UXPROD-1735.sql
\ir UXPROD-1539.sql
\ir patch_hstry_upd.sql
\echo
\echo MARCCAT DB 1.2 - End ---------------------------------------------------------------
\echo
