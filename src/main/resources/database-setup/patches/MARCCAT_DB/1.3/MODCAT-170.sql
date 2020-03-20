CREATE TABLE OLISUITE.T_TM_CBW
   (TBL_SEQ_NBR bigint NOT NULL, 
	TBL_VLU_CDE character(1) NOT NULL,
	TBL_VLU_OBSLT_IND character(1) NOT NULL, 
	SHORT_STRING_TEXT character varying(60) NOT NULL,
	STRING_TEXT character varying(192) NOT NULL,
	LANGID character(3) NOT NULL,
	  CONSTRAINT t_tm_cbw_pk PRIMARY KEY (tbl_vlu_cde, langid)
   );

COMMENT ON TABLE olisuite.T_TM_CBW IS 'Contains the codes for tactile material: Class of Braille Writing.';

Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Literary braille','Literary braille','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Format code braille','Format code braille','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Mathemat. Scientific','Mathematics Scientific braille','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Computer braille','Computer braille','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Music braille','Music braille','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'m','0','Mutiple braille tpe','Mutiple braille types','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'n','0','Not applicable','Not applicable','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Unknown','Unknown','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Other','Other','eng');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Literary braille','Literary braille','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Format code braille','Format code braille','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Mathemat. Scientific','Mathematics Scientific braille','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Computer braille','Computer braille','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Music braille','Music braille','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'m','0','Mutiple braille tpe','Mutiple braille types','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'n','0','Not applicable','Not applicable','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Unknown','Unknown','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Other','Other','fra');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Braille letterario','Braille letterario','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Codice form. braille','Codice formato braille','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Matematico scientif.','Braille matematico scientifico','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Braille computer','Braille computer','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Braille musica','Braille musica','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'m','0','Tipi multip. braille','Tipi multipli braille','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'n','0','Non applicabile','Non applicabile','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Sconosciuto','Sconosciuto','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Altro','Altro','ita');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Literatura braille','Literatura braille','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Form. código Braille','Formato código Braille','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Matemát. cientificas','Matemáticas cientificas braille','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Ordenador braille ','Ordenador braille ','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Música braille','Música braille','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'m','0','Múltip. tipos braill','Múltiples tipos de braille','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'n','0','No aplicable','No aplicable','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Desconocido','Desconocido','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Otro','Otro','spa');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Literary braille','Literary braille','hun');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Format code braille','Format code braille','hun');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Mathemat. Scientific','Mathematics Scientific braille','hun');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Computer braille','Computer braille','hun');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Music braille','Music braille','hun');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'m','0','Mutiple braille tpe','Mutiple braille types','hun');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'n','0','Not applicable','Not applicable','hun');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Unknown','Unknown','hun');
Insert into OLISUITE.T_TM_CBW (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Other','Other','hun');

ALTER TABLE amicus.tm DROP CONSTRAINT tm_fk3;
ALTER TABLE amicus.tm DROP CONSTRAINT tm_fk4;

CREATE TABLE OLISUITE.T_TM_SMD 
   (TBL_SEQ_NBR bigint NOT NULL,
	TBL_VLU_CDE CHAR(1) NOT NULL,
	TBL_VLU_OBSLT_IND CHAR(1) NOT NULL, 
	SHORT_STRING_TEXT CHARACTER VARYING(60) NOT NULL,
	STRING_TEXT CHARACTER VARYING(192) NOT NULL,
	LANGID CHAR(3) NOT NULL,
    CONSTRAINT t_tm_smd_pk PRIMARY KEY (tbl_vlu_cde, langid)
   );

COMMENT ON TABLE olisuite.T_TM_SMD IS 'Contains the codes for tactile material: Specific Material Designation.';
   
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Moon','Moon','eng');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Braille','Braille','eng');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Combination','Combination','eng');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Tactile','Tactile','eng');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Unspecified','Unspecified','eng');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Other','Other','eng');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Moon','Moon','fra');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Braille','Braille','fra');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Combination','Combination','fra');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Tactile','Tactile','fra');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Unspecified','Unspecified','fra');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Other','Other','fra');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Luna','Luna','ita');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Braille','Braille','ita');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Combinazione','Combinazione','ita');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Tattile','Tattile','ita');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Non specificato','Non specificato','ita');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Altro','Altro','ita');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Luna','Luna','spa');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Braille','Braille','spa');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Combinación','Combinación','spa');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Táctil','Táctil','spa');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','No especificado','No especificado','spa');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Otro','Otro','spa');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Moon','Moon','hun');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Braille','Braille','hun');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Combination','Combination','hun');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Tactile','Tactile','hun');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Unspecified','Unspecified','hun');
Insert into OLISUITE.T_TM_SMD (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Other','Other','hun');
   
   
CREATE TABLE OLISUITE.T_TM_LC 
   (TBL_SEQ_NBR bigint NOT NULL,
	TBL_VLU_CDE CHAR(1) NOT NULL,
	TBL_VLU_OBSLT_IND CHAR(1) NOT NULL, 
	SHORT_STRING_TEXT CHARACTER VARYING(60) NOT NULL,
	STRING_TEXT CHARACTER VARYING(192) NOT NULL,
	LANGID CHAR(3) NOT NULL,
    CONSTRAINT t_tm_lc_pk PRIMARY KEY (tbl_vlu_cde, langid)
   );   

COMMENT ON TABLE olisuite.T_TM_LC  IS 'Contains the codes for tactile material: Level of Contraction.';

Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Uncontracted','Uncontracted','eng');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Contracted','Contracted','eng');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'m','0','Combination','Combination','eng');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'n','0','Not applicable','Not applicable','eng');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Unknown','Unknown','eng');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Other','Other','eng');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Uncontracted','Uncontracted','fra');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Contracted','Contracted','fra');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'m','0','Combination','Combination','fra');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'n','0','Not applicable','Not applicable','fra');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Unknown','Unknown','fra');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Other','Other','fra');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Non contratto ','Non contratto','ita');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Contratto ','Contratto','ita');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'m','0','Combinazione ','Combinazione','ita');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'n','0','Non applicabile','Non applicabile','ita');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Sconosciuto','Sconosciuto','ita');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Altro','Altro','ita');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','No contraída','No contraída','spa');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Contraido','Contraido','spa');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'m','0','Combinación','Combinación','spa');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'n','0','No aplicable','No aplicable','spa');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Desconocido','Desconocido','spa');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Otro','Otro','spa');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Uncontracted','Uncontracted','hun');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Contracted','Contracted','hun');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'m','0','Combination','Combination','hun');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'n','0','Not applicable','Not applicable','hun');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'u','0','Unknown','Unknown','hun');
Insert into OLISUITE.T_TM_LC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'z','0','Other','Other','hun');

CREATE TABLE OLISUITE.T_TM_BMF
   (TBL_SEQ_NBR bigint NOT NULL,
	TBL_VLU_CDE CHAR(1) NOT NULL,
	TBL_VLU_OBSLT_IND CHAR(1) NOT NULL, 
	SHORT_STRING_TEXT CHARACTER VARYING(60) NOT NULL,
	STRING_TEXT CHARACTER VARYING(192) NOT NULL,
	LANGID CHAR(3) NOT NULL,
    CONSTRAINT t_tm_bmf_pk PRIMARY KEY (tbl_vlu_cde, langid)
   );
   
COMMENT ON TABLE olisuite.T_TM_BMF IS 'Contains the codes for tactile material: Braille Music Format.';

Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Bar over Bar','Bar over Bar','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Bar by Bar','Bar by Bar','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Line over Line','Line over Line','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Paragraph','Paragraph','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Single line','Single line','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Section by Section','Section by Section','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Line by Line','Line by Line','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'h','0','Open Score','Open Score','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'i','0','Spanner short frm','Spanner short form','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (100,'j','0','Short frm scoring','Short form scoring','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (110,'k','0','Outline','Outline','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (120,'l','0','Vertical Score','Vertical Score','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (130,'n','0','Not applicable','Not applicable','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (140,'u','0','Unknown','Unknown','eng');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Bar over Bar','Bar over Bar','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Bar by Bar','Bar by Bar','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Line over Line','Line over Line','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Paragraph','Paragraph','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Single line','Single line','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Section by Section','Section by Section','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Line by Line','Line by Line','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'h','0','Open Score','Open Score','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'i','0','Spanner short frm','Spanner short form','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (100,'j','0','Short frm scoring','Short form scoring','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (110,'k','0','Outline','Outline','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (120,'l','0','Vertical Score','Vertical Score','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (130,'n','0','Not applicable','Not applicable','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (140,'u','0','Unknown','Unknown','fra');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Riga su riga','Riga su riga','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Riga per riga','Riga per riga','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Linea su linea','Linea su linea','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Paragrafo','Paragrafo','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Linea singola','Linea singola','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Sezione per sezione','Sezione per sezione','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Linea per linea','Linea per linea','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'h','0','Partitura aperta','Partitura aperta','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'i','0','Chiave breve','Chiave breve','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (100,'j','0','Partitura breve','Partitura breve','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (110,'k','0','Schema','Schema','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (120,'l','0','Partitura verticale','Partitura verticale','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (130,'n','0','Non applicabile','Non applicabile','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (140,'u','0','Sconosciuto','Sconosciuto','ita');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Barra sobre barra','Barra sobre barra','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Barra a barra','Barra a barra','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Línea sobre línea','Línea sobre línea','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Párrafo','Párrafo','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','única línea','única línea','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Sección a sección','Sección a sección','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Línea a línea','Línea a línea','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'h','0','Abrir puntuación','Abrir puntuación','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'i','0','Spanner short frm','Spanner short form','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (100,'j','0','Forma br. puntuación','Forma breve puntuación','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (110,'k','0','Perfil','Perfil','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (120,'l','0','Linea vertical','Linea vertical','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (130,'n','0','No aplicable','No aplicable','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (140,'u','0','Desconocido','Desconocido','spa');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Bar over Bar','Bar over Bar','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Bar by Bar','Bar by Bar','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Line over Line','Line over Line','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Paragraph','Paragraph','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Single line','Single line','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Section by Section','Section by Section','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Line by Line','Line by Line','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'h','0','Open Score','Open Score','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'i','0','Spanner short frm','Spanner short form','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (100,'j','0','Short frm scoring','Short form scoring','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (110,'k','0','Outline','Outline','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (120,'l','0','Vertical Score','Vertical Score','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (130,'n','0','Not applicable','Not applicable','hun');
Insert into OLISUITE.T_TM_BMF (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (140,'u','0','Unknown','Unknown','hun');   

CREATE TABLE OLISUITE.T_TM_SPC
   (TBL_SEQ_NBR bigint NOT NULL,
	TBL_VLU_CDE CHAR(1) NOT NULL,
	TBL_VLU_OBSLT_IND CHAR(1) NOT NULL, 
	SHORT_STRING_TEXT CHARACTER VARYING(60) NOT NULL,
	STRING_TEXT CHARACTER VARYING(192) NOT NULL,
	LANGID CHAR(3) NOT NULL,
    CONSTRAINT t_tm_spc_pk PRIMARY KEY (tbl_vlu_cde, langid)
   );
   
COMMENT ON TABLE olisuite.T_TM_SPC IS 'Contains the codes for tactile material: Special Physical Characteristics.';

Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Print/braille','Print/braille','eng');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Jumbo braille','Jumbo or enlarged braille','eng');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'n','0','Not applicable','Not applicable','eng');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'u','0','Unknown','Unknown','eng');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'z','0','Other','Other','eng');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Print/braille','Print/braille','fra');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Jumbo braille','Jumbo or enlarged braille','fra');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'n','0','Not applicable','Not applicable','fra');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'u','0','Unknown','Unknown','fra');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'z','0','Other','Other','fra');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Stampa/braille','Stampa/braille','ita');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Braille ingigantito','Braille ingrandito o ingigantito','ita');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'n','0','Non applicabile','Non applicabile','ita');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'u','0','Sconosciuto','Sconosciuto','ita');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'z','0','Altro','Altro','ita');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Impreso/braille','Impreso/braille','spa');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Jumbo o Braille aum.','Jumbo o Braille aumentado','spa');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'n','0','No aplicable','No aplicable','spa');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'u','0','Desconocido','Desconocido','spa');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'z','0','Otro','Otro','spa');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Print/braille','Print/braille','hun');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Jumbo braille','Jumbo or enlarged braille','hun');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'n','0','Not applicable','Not applicable','hun');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'u','0','Unknown','Unknown','hun');
Insert into OLISUITE.T_TM_SPC (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'z','0','Other','Other','hun');
