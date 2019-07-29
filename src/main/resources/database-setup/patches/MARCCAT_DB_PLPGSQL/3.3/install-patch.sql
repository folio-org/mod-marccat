\echo - MARCCAT DB (PLPGSQL)
\echo - Ver. 3.3
\echo - @Cult s.r.l.
\echo - Date: 26/07/2019
\echo - PostgreSQL compatibility: 9.6+
\echo
\echo MARCCAT DB PLPGSQL 3.3 - Start -----------------------------------------------------
\echo
set myvars.patch_comp_typ TO :'patch_comp_typ';
set myvars.patch_sp_nbr TO :'patch_sp_nbr';
set myvars.patch_rel_nbr TO :'patch_rel_nbr';
\set ON_ERROR_STOP on
\ir patch-check.sql
\set ON_ERROR_STOP off
\ir clstn_itm_acs_pnt_d.sql
\ir clstn_itm_acs_pnt_iu.sql
\ir trigger_fct_clstn_d_1.3.sql
\ir hldg_nte_d.sql
\ir trigger_fct_hldg_nte_d_1.3.sql
\ir nme_acs_pnt_plus_refs_d.sql
\ir nme_acs_pnt_plus_refs_iu.sql
\ir trigger_fct_nme_hdg_d_1.3.sql
\ir publ_acs_pnt_plus_refs_d.sql
\ir publ_acs_pnt_plus_refs_iu.sql
\ir trigger_fct_publ_acs_pnt_d.sql
\ir trigger_fct_publ_acs_pnt_iu.sql
\ir trigger_fct_publ_hdg_d_1.3.sql
\ir trigger_fct_publ_tag_d.sql
\ir trigger_fct_publ_tag_iu.sql
\ir sbjct_acs_pnt_plus_refs_d.sql
\ir sbjct_acs_pnt_plus_refs_iu.sql
\ir trigger_fct_sbjct_hdg_d_1.3.sql
\ir trigger_fct_bib_nte_d_1.3.sql
\ir trigger_fct_bib_nte_ovrfw_d_1.3.sql
\ir trigger_fct_cpy_id_d.sql
\ir ttl_acs_pnt_plus_refs_d.sql
\ir ttl_acs_pnt_plus_refs_iu.sql
\ir patch_hstry_upd.sql
\echo
\echo MARCCAT DB PLPGSQL 3.3 - End -------------------------------------------------------
\echo
