ALTER TABLE amicus.mic DROP CONSTRAINT mic_fk7;

ALTER TABLE amicus.mic DROP CONSTRAINT mic_fk10;

ALTER TABLE amicus.mic
    ADD CONSTRAINT mic_fk10 FOREIGN KEY (mic_gnrtn_cde)
    REFERENCES amicus.t_mic_gnrtn (tbl_vlu_cde) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE;

ALTER TABLE amicus.mic DROP CONSTRAINT mic_fk8;

ALTER TABLE amicus.mic
    ADD CONSTRAINT mic_fk8 FOREIGN KEY (mic_clr_cde)
    REFERENCES amicus.t_mic_clr (tbl_vlu_cde) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE;

ALTER TABLE amicus.mic DROP CONSTRAINT mic_fk9;

ALTER TABLE amicus.mic
    ADD CONSTRAINT mic_fk9 FOREIGN KEY (mic_flm_emlsn_cde)
    REFERENCES amicus.t_mic_flm_emlsn (tbl_vlu_cde) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    DEFERRABLE;
    
Insert into OLISUITE.T_MP_DTRTN_STGE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (120, '|', '0', 'No attempt to code' ,'No attempt to code', 'eng');
Insert into OLISUITE.T_MP_DTRTN_STGE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (120, '|', '0', 'Nessun tentativo di codifica' ,'Nessun tentativo di codifica', 'ita');
Insert into AMICUS.T_MP_DTRTN_STGE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,TBL_VLU_ACTV_STRT_DTE,TBL_VLU_ACTV_END_DTE, TBL_SHRT_ENG_TXT, TBL_SHRT_FR_TXT, TBL_LNG_ENG_TXT, TBL_LNG_FR_TXT) 
values (120, '|', '0', now(), null, 'No attempt to code', 'Nessun tentativo di codifica', 'No attempt to code', 'Nessun tentativo di codifica');

Insert into OLISUITE.T_MP_PROD_ELEM (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (90, '|', '0', 'No attempt to code' ,'No attempt to code', 'eng');
Insert into OLISUITE.T_MP_PROD_ELEM (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) 
values (90, '|', '0', 'Nessun tentativo di codifica' ,'Nessun tentativo di codifica', 'ita');
Insert into AMICUS.T_MP_PROD_ELEM (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,TBL_VLU_ACTV_STRT_DTE,TBL_VLU_ACTV_END_DTE, TBL_SHRT_ENG_TXT, TBL_SHRT_FR_TXT, TBL_LNG_ENG_TXT, TBL_LNG_FR_TXT) 
values (90, '|', '0', now(), null, 'No attempt to code', 'Nessun tentativo di codifica', 'No attempt to code', 'Nessun tentativo di codifica');    
