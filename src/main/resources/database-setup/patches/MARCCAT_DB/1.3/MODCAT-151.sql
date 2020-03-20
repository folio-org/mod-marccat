CREATE TABLE olisuite.t_crtgc_mtrl
(
    tbl_seq_nbr bigint NOT NULL,
    tbl_vlu_cde character(1) NOT NULL,
    tbl_vlu_obslt_ind character(1) NOT NULL,
    short_string_text character varying(60) NOT NULL,
    string_text character varying(192) NOT NULL,
    langid character(3) NOT NULL,
      CONSTRAINT t_crtgc_mtrl_pk PRIMARY KEY (tbl_vlu_cde, langid)
);

COMMENT ON TABLE olisuite.t_crtgc_mtrl IS 'Contains the codes for the cartographic material.';

Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Map series','Map series','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Map serial','Map serial','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Globe','Globe','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Atlas','Atlas','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Sep map supp','Seperate map supplement to another work','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Single map','Single map','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Map bound','Map bound part of another work','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Unknown','Unknown','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Other','Other','eng');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Map series','Map series','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Map serial','Map serial','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Globe','Globe','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Atlas','Atlas','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Sep map supp','Seperate map supplement to another work','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Single map','Single map','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Map bound','Map bound part of another work','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Unknown','Unknown','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Autre','Autre','fra');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Serie di c. geogr.','Serie di carte geografiche','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','C. geogr. ser.','Carta geografica seriale','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Mappamondo','Mappamondo','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Atlante','Atlante','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','C. geogr.suppl.sep','Carta geografica separata supplemento di un''altra opera','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','C. geogr. singola','Carta geografica singola','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','C. geogr. rilegata','Carta geografica rilegata parte di un''altra opera','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Sconosciuto','Sconosciuto','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Altro','Altro','ita');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Colecciones de mapas','Colecciones de mapas','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Mapas, periódicas','Mapas, periódicas','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Globos','Globos','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Atlas','Atlas','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Mapa separado suplem','Mapa separado suplemento a otra obra','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Mapa sencillo','Mapa sencillo','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Mapa encuadernado','Mapa encuadernado como parte de otra obra','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Desconocido','Desconocido','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Otros','Otros','spa');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'b','0','Topográfiai sorozat','Topográfiai sorozat','hun');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'c','0','Térkép (idõszaki)','Térkép (idõszaki)','hun');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'d','0','Glóbusz','Glóbusz','hun');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (50,'e','0','Atlasz','Atlasz','hun');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Térkép','Térkép','hun');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (90,'z','0','Egyéb','Egyéb','hun');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (80,'u','0','Ismeretlen','Ismeretlen','hun');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (70,'g','0','Térk. belekötve','Térkép belekötve egy másik mube','hun');
Insert into OLISUITE.T_CRTGC_MTRL (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (60,'f','0','Térk. mell.','Térkép mellékletként egy másik muben','hun');
