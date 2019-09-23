\echo - MARCCAT DB
\echo - Ver. 1.2
\echo - @Cult s.r.l.
\echo - Date: 26/07/2019
\echo - PostgreSQL compatibility: 9.6+
\echo
\echo MARCCAT DB 1.2 - Start -------------------------------------------------------------
\echo
\ir init_template.sql
\ir MODCAT-91.sql
\ir UXPROD-1735.sql
\ir UXPROD-1539.sql
\ir patch_hstry_upd.sql
\echo
\echo MARCCAT DB 1.2 - End ---------------------------------------------------------------
\echo
