Insert into OLISUITE.T_TTL_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (0,0,'0','Heading used in NT heading','Heading used in NT heading','eng');
Insert into OLISUITE.T_TTL_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (0,0,'0','Heading used in NT heading','Intestazione usata in intestazione NT','ita');
Insert into AMICUS.T_TTL_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,TBL_VLU_ACTV_STRT_DTE,TBL_VLU_ACTV_END_DTE,TBL_SHRT_ENG_TXT,TBL_SHRT_FR_TXT,TBL_LNG_ENG_TXT,TBL_LNG_FR_TXT) 
values (0,0,'0',now(),null,'Heading used in NT heading','Intestazione usata in intestazione NT','Heading used in NT heading','Intestazione usata in intestazione NT');

Insert into OLISUITE.T_NME_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (0,0,'0','Heading used in NT heading','Heading used in NT heading','eng');
Insert into OLISUITE.T_NME_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (0,0,'0','Heading used in NT heading','Intestazione usata in intestazione NT','ita');
Insert into AMICUS.T_NME_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,TBL_VLU_ACTV_STRT_DTE,TBL_VLU_ACTV_END_DTE,TBL_SHRT_ENG_TXT,TBL_SHRT_FR_TXT,TBL_LNG_ENG_TXT,TBL_LNG_FR_TXT) 
values (0,0,'0',now(),null,'Heading used in NT heading','Intestazione usata in intestazione NT','Heading used in NT heading','Intestazione usata in intestazione NT');

Insert into OLISUITE.T_TTL_SCDRY_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (0,0,'0','Heading used in NT heading','Heading used in NT heading','eng');
Insert into OLISUITE.T_TTL_SCDRY_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (0,0,'0','Heading used in NT heading','Intestazione usata in intestazione NT','ita');
Insert into AMICUS.T_TTL_SCDRY_FNCTN (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,TBL_VLU_ACTV_STRT_DTE,TBL_VLU_ACTV_END_DTE,TBL_SHRT_ENG_TXT,TBL_SHRT_FR_TXT,TBL_LNG_ENG_TXT,TBL_LNG_FR_TXT) 
values (0,0,'0',now(),null,'Heading used in NT heading','Intestazione usata in intestazione NT','Heading used in NT heading','Intestazione usata in intestazione NT');

ALTER TABLE amicus.ttl_acs_pnt_plus_refs DROP CONSTRAINT ttl_acs_pnt_plus_refs_pk;

ALTER TABLE amicus.ttl_acs_pnt_plus_refs
    ADD CONSTRAINT ttl_acs_pnt_plus_refs_pk PRIMARY KEY (ttl_hdg_nbr, bib_itm_nbr, usr_vw_ind, ttl_fnctn_cde);
