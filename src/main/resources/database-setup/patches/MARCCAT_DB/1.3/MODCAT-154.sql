CREATE TABLE olisuite.t_book_typ_cde
(
    tbl_seq_nbr bigint NOT NULL,
    tbl_vlu_cde character(1) NOT NULL,
    tbl_vlu_obslt_ind character(1) NOT NULL,
    short_string_text character varying(60) NOT NULL,
    string_text character varying(192) NOT NULL,
    langid character(3) NOT NULL,
      CONSTRAINT t_book_typ_cde_pk PRIMARY KEY (tbl_vlu_cde, langid)
);

COMMENT ON TABLE olisuite.t_book_typ_cde IS 'Contains the codes for the book types.';

Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Books','Books','eng');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'t','0','Manuscript','Manuscript language material','eng');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Books','Books','fra');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'t','0','Manuscript','Manuscript language material','fra');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Libri','Libri','ita');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'t','0','Manoscritto','Manoscritto','ita');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'x','0','Libros','Libros','spa');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'t','0','Mat textual manuscr','Material textual manuscrito','spa');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Libros','Libros','spa');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'a','0','Könyv','Könyv','hun');
Insert into OLISUITE.T_BOOK_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'t','0','Kézirat','Kézirat nyelv dokumentum','hun');

CREATE TABLE olisuite.t_crtgc_typ_cde
(
    tbl_seq_nbr bigint NOT NULL,
    tbl_vlu_cde character(1) NOT NULL,
    tbl_vlu_obslt_ind character(1) NOT NULL,
    short_string_text character varying(60) NOT NULL,
    string_text character varying(192) NOT NULL,
    langid character(3) NOT NULL,
      CONSTRAINT t_crtgc_typ_cde_pk PRIMARY KEY (tbl_vlu_cde, langid)
);

COMMENT ON TABLE olisuite.t_crtgc_typ_cde IS 'Contains the codes for the cartographic material.';

Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'e','0','Cartographic','Cartographic material','eng');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'f','0','Manuscript','Manuscript cartograhic material','eng');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'e','0','Cartographic','Cartographic material','fra');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'f','0','Manuscript','Manuscript cartograhic material','fra');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'e','0','Cartografico','Materiale cartografico','ita');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'f','0','Manoscritto','Materiale cartografico manoscritto','ita');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'e','0','Cartográfico','Cartográfico','spa');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'f','0','Cartográfico manuscr','Cartográfico manuscrito','spa');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'e','0','KartogrDok','Kartográfiai dokumentum','hun');
Insert into OLISUITE.T_CRTGC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'f','0','KéziratosKartogrDok','Kéziratos kartográfiai dokumentum','hun');

CREATE TABLE olisuite.t_msc_typ_cde
(
    tbl_seq_nbr bigint NOT NULL,
    tbl_vlu_cde character(1) NOT NULL,
    tbl_vlu_obslt_ind character(1) NOT NULL,
    short_string_text character varying(60) NOT NULL,
    string_text character varying(192) NOT NULL,
    langid character(3) NOT NULL,
      CONSTRAINT t_msc_typ_cde_pk PRIMARY KEY (tbl_vlu_cde, langid)
);

COMMENT ON TABLE olisuite.t_msc_typ_cde IS 'Contains the codes for the musical material.';

Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'c','0','Printed','Printed music','eng');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'d','0','Manuscript','Manuscript music','eng');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'i','0','Nonmusical','Nonmusical sound recording','eng');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'j','0','Musical','Musical sound recording','eng');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'c','0','Printed','Printed music','fra');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'d','0','Manuscript','Manuscript music','fra');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'i','0','Nonmusical','Nonmusical sound recording','fra');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'j','0','Musical','Musical sound recording','fra');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'c','0','Stampata','Musica stampata','ita');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'d','0','Manoscritta','Musica manoscritta','ita');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'i','0','Non musicale','Registrazione suoni non musicali','ita');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'j','0','Musicale','Registrazione suoni musicali','ita');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'c','0','Música impresa','Música impresa','spa');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'d','0','Música manuscrita','Música manuscrita','spa');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'i','0','GS no musicales','Grabaciones de sonido no musicales','spa');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'j','0','GS musicales','Grabaciones de sonido musicales','spa');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'c','0','Nyomtatott','Nyomtatott zenemu','hun');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'d','0','Kéziratos','Kéziratos zenemu','hun');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'i','0','Nem zenei','Nem zenei hangzó anyag','hun');
Insert into OLISUITE.T_MSC_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'j','0','Zenei','Zenei hangzó anyag','hun');

CREATE TABLE olisuite.t_vsl_typ_cde
(
    tbl_seq_nbr bigint NOT NULL,
    tbl_vlu_cde character(1) NOT NULL,
    tbl_vlu_obslt_ind character(1) NOT NULL,
    short_string_text character varying(60) NOT NULL,
    string_text character varying(192) NOT NULL,
    langid character(3) NOT NULL,
      CONSTRAINT t_vsl_typ_cde_pk PRIMARY KEY (tbl_vlu_cde, langid)
);

COMMENT ON TABLE olisuite.t_vsl_typ_cde IS 'Contains the codes for the visual material.';

Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'g','0','Projected','Projected medium','eng');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'k','0','Two-dim nonproj.','Two-dimensional nonprojectible graphic','eng');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'o','0','Kit','Kit','eng');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'r','0','Three-dim artifact','Three-dimensional artifact or naturally occurring object','eng');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'g','0','Projected','Projected medium','fra');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'k','0','Two-dim nonproj.','Two-dimensional nonprojectible graphic','fra');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'o','0','Kit','Kit','fra');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'r','0','Three-dim artifact','Three-dimensional artifact or naturally occurring object','fra');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'g','0','Proiettato','Mezzo proiettato','ita');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'k','0','Bidim. no proiet.','Grafico bidimensionale non proiettabile','ita');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'o','0','Kit','Kit','ita');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'r','0','Manufatto tridim.','Manufatto o oggetto naturale tridimensionale','ita');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'g','0','Proyectado','Proyectado','spa');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'k','0','2-D no proyectable','Gráfico no proyectable en dos dimensiones','spa');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'o','0','Kit','Kit','spa');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'r','0','3-D o natural','Objeto tridimensional o natural','spa');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (10,'g','0','Audiovizuális anyag','Audiovizuális anyag','hun');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (20,'k','0','Két-dim graf','Két-dimenziós nem-vetíthetõ grafika','hun');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (30,'o','0','Készlet','Készlet','hun');
Insert into OLISUITE.T_VSL_TYP_CDE (TBL_SEQ_NBR,TBL_VLU_CDE,TBL_VLU_OBSLT_IND,SHORT_STRING_TEXT,STRING_TEXT,LANGID) values (40,'r','0','Három-dim alkotás','Három-dimenziós alkotás vagy természetes tárgy','hun');
