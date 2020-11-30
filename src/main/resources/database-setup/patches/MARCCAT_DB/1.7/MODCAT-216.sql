--
-- Name: t_aut_ref_stus; Type: TABLE; Schema: olisuite; Owner: -
--

CREATE TABLE olisuite.t_aut_ref_stus (
    tbl_seq_nbr bigint NOT NULL,
    tbl_vlu_cde character(1) NOT NULL,
    tbl_vlu_obslt_ind character(1) NOT NULL,
    short_string_text character varying(60) NOT NULL,
    string_text character varying(192) NOT NULL,
    langid character(3) NOT NULL
);

--ALTER TABLE olisuite.t_aut_ref_stus OWNER TO :user_name;

--
-- Name: TABLE t_aut_ref_stus; Type: COMMENT; Schema: olisuite; Owner: -
--

COMMENT ON TABLE olisuite.t_aut_ref_stus IS 'Contains the codes for the AUT_REF_STUS_CDE table columns.';


--
-- Data for Name: t_aut_ref_stus; Type: TABLE DATA; Schema: olisuite; Owner: -
--
Insert into Olisuite.T_AUT_REF_STUS  Values(50, ' ', '0', 'Value used in record', 'Value already used', 'eng');
Insert into Olisuite.T_AUT_REF_STUS  Values(50, ' ', '0', 'Valeur utilisee', 'Valeur utilisee dans notices creees avant la def. du multiplet', 'fra');
Insert into Olisuite.T_AUT_REF_STUS  Values(50, ' ', '0', 'Valore già usato', 'Valore già usato', 'ita');
Insert into Olisuite.T_AUT_REF_STUS  Values(50, ' ', '0', 'Valor utilizado', 'Valor utilizado en el registro anterior a la def.  de este campo', 'spa');
Insert into Olisuite.T_AUT_REF_STUS  Values(30, 'a', '0', 'Reference evaluated', 'References have been evaluated', 'eng');
Insert into Olisuite.T_AUT_REF_STUS  Values(30, 'a', '0', 'Renvois compares', 'Renvois ont ete compares', 'fra');
Insert into Olisuite.T_AUT_REF_STUS  Values(30, 'a', '0', 'Rinvii valutati', 'Rinvii valutati', 'ita');
Insert into Olisuite.T_AUT_REF_STUS  Values(30, 'a', '0', 'Referencia evaluada', 'Referencias evaluadas', 'spa');
Insert into Olisuite.T_AUT_REF_STUS  Values(40, 'b', '0', 'Ref not evaluated', 'Refs not been evaluated', 'eng');
Insert into Olisuite.T_AUT_REF_STUS  Values(40, 'b', '0', 'Renvois non compares', 'Renvois n''ont pas ete compares', 'fra');
Insert into Olisuite.T_AUT_REF_STUS  Values(40, 'b', '0', 'Rinvii non valutati', 'Rinvii non valutati', 'ita');
Insert into Olisuite.T_AUT_REF_STUS  Values(40, 'b', '0', 'Referen. no evaluada', 'Referencias no evaluadas', 'spa');
Insert into Olisuite.T_AUT_REF_STUS  Values(20, 'n', '0', 'Not applicable', 'Not applicable', 'eng');
Insert into Olisuite.T_AUT_REF_STUS  Values(20, 'n', '0', 'Sans objet', 'Sans objet', 'fra');
Insert into Olisuite.T_AUT_REF_STUS  Values(20, 'n', '0', 'Non applicabile', 'Non applicabile', 'ita');
Insert into Olisuite.T_AUT_REF_STUS  Values(20, 'n', '0', 'No aplicable', 'No aplicable', 'spa');

--
-- Name: t_aut_ref_stus t_aut_ref_stus_pk; Type: CONSTRAINT; Schema: olisuite; Owner: -
--

ALTER TABLE ONLY olisuite.t_aut_ref_stus
    ADD CONSTRAINT t_aut_ref_stus_pk PRIMARY KEY (tbl_vlu_cde, langid);

--
-- Name: t_aut_ref_stus_tbl_vlu_cde_langid_idx; Type: INDEX; Schema: olisuite; Owner: -
--

CREATE UNIQUE INDEX t_aut_ref_stus_tbl_vlu_cde_langid_idx ON olisuite.t_aut_ref_stus USING btree (tbl_vlu_cde, langid);

--
-- Name: TABLE t_aut_ref_stus; Type: ACL; Schema: olisuite; Owner: -
--

--GRANT ALL ON TABLE olisuite.t_aut_ref_stus TO :user_name;


--
-- Name: t_aut_rec_mdftn; Type: TABLE; Schema: olisuite; Owner: -
--

CREATE TABLE olisuite.t_aut_rec_mdftn (
    tbl_seq_nbr bigint NOT NULL,
    tbl_vlu_cde character(1) NOT NULL,
    tbl_vlu_obslt_ind character(1) NOT NULL,
    short_string_text character varying(60) NOT NULL,
    string_text character varying(192) NOT NULL,
    langid character(3) NOT NULL
);

--ALTER TABLE olisuite.t_aut_rec_mdftn OWNER TO :user_name;

--
-- Name: TABLE t_aut_rec_mdftn; Type: COMMENT; Schema: olisuite; Owner: -
--

COMMENT ON TABLE olisuite.t_aut_rec_mdftn IS 'Contains the codes for the AUT_REC_MDFTN_CDE table columns.';


--
-- Data for Name: t_aut_rec_mdftn; Type: TABLE DATA; Schema: olisuite; Owner: -
--
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (20, ' ', '0', 'Record not modified', 'Record not modified', 'eng');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (20, ' ', '0', 'Notice non modifiee', 'Notice non modifiee', 'fra');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (20, ' ', '0', 'Record non modif.', 'Record non modificato', 'ita');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (20, ' ', '0', 'No modificado', 'No modificado', 'spa');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (40, 's', '0', 'Record shortened', 'Record shortened', 'eng');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (40, 's', '0', 'Notice abregee', 'Notice abregee', 'fra');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (40, 's', '0', 'Record abbreviato', 'Record abbreviato', 'ita');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (40, 's', '0', 'Registro abreviado', 'Registro abreviado', 'spa');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (30, 'x', '0', 'Characters not input', 'Non input chars. present', 'eng');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (30, 'x', '0', 'Caract. non enreg.', 'Notice contient des caracteres qui ne peuvent etre enregistres', 'fra');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (30, 'x', '0', 'Carat. non registrab', 'Record con caratteri non registrabili', 'ita');
Insert into Olisuite.T_AUT_REC_MDFTN  Values  (30, 'x', '0', 'Caracteres omitidos', 'Registro con caracteres omitidos', 'spa');
COMMIT;


--
-- Name: t_aut_rec_mdftn t_aut_rec_mdftn_pk; Type: CONSTRAINT; Schema: olisuite; Owner: -
--

ALTER TABLE ONLY olisuite.t_aut_rec_mdftn
    ADD CONSTRAINT t_aut_rec_mdftn_pk PRIMARY KEY (tbl_vlu_cde, langid);

--
-- Name: t_aut_rec_mdftn_tbl_vlu_cde_langid_idx; Type: INDEX; Schema: olisuite; Owner: -
--

CREATE UNIQUE INDEX t_aut_rec_mdftn_tbl_vlu_cde_langid_idx ON olisuite.t_aut_rec_mdftn USING btree (tbl_vlu_cde, langid);

--
-- Name: TABLE t_aut_rec_mdftn; Type: ACL; Schema: olisuite; Owner: -
--

--GRANT ALL ON TABLE olisuite.t_aut_rec_mdftn TO :user_name;

--
-- Name: t_aut_ctlgg_src; Type: TABLE; Schema: olisuite; Owner: -
--

CREATE TABLE olisuite.t_aut_ctlgg_src (
    tbl_seq_nbr bigint NOT NULL,
    tbl_vlu_cde character(1) NOT NULL,
    tbl_vlu_obslt_ind character(1) NOT NULL,
    short_string_text character varying(60) NOT NULL,
    string_text character varying(192) NOT NULL,
    langid character(3) NOT NULL
);

--ALTER TABLE olisuite.t_aut_ctlgg_src OWNER TO :user_name;

--
-- Name: TABLE t_aut_ctlgg_src; Type: COMMENT; Schema: olisuite; Owner: -
--

COMMENT ON TABLE olisuite.t_aut_ctlgg_src IS 'Contains the codes for the AUT_CTLGG_SRC_CDE table columns.';


--
-- Data for Name: t_aut_ctlgg_src; Type: TABLE DATA; Schema: olisuite; Owner: -
--
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (150, ' ', '0', 'Nat agency', 'National bibliographic agency', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (150, ' ', '0', 'Ag Bfca Nac', 'Agencia Bibliográfica Nacional', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (150, ' ', '0', 'Nat agency', 'National bibliographic agency', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (150, ' ', '0', 'Ag. bib. naz.', 'Agenzia bibliografica nazionale', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (160, 'a', '1', 'US Bib Naz agr(OBS)', 'Biblioteca Nazionale dell''Agricoltura-USA(OBS)', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (160, 'a', '1', 'US NAL OBS', 'US National Agriculture Library OBS', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (160, 'a', '1', 'US Nat Agr Lib (OBS)', 'US National Agriculture Library (OBSOLETE)', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (160, 'a', '1', 'US Nat Agr Lib (OBS)', 'US National Agriculture Library (OBSOLETE)', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (170, 'b', '1', 'US NLM OBS', 'US National Library of Medicine OBS', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (170, 'b', '1', 'Bib Naz Med USA(OBS)', 'Biblioteca Nazionale di Medicina-USA(OBS)', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (170, 'b', '1', 'US Nat Lib Med (OBS)', 'US National Library of Medicine (OBSOLETE)', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (170, 'b', '1', 'US Nat Lib Med (OBS)', 'US National Library of Medicine (OBSOLETE)', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (50, 'c', '0', 'Co-op. catlg progr', 'Co-operative cataloguing programmes', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (50, 'c', '0', 'Progr catalog coop', 'Programmi di catalogazione cooperativo', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (50, 'c', '0', 'Co-op. catlg progr', 'Co-operative cataloguing programmes', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (50, 'c', '0', 'Cat Coop LC', 'Programa de Catalogación Cooperativa de la LC', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (100, 'd', '1', 'Autres sources', 'Autres sources', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (100, 'd', '1', 'Altre fonti', 'Altre fonti', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (100, 'd', '0', 'Otras fuentes', 'Otras fuentes', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (100, 'd', '1', 'Other sources', 'Other sources', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (180, 'h', '1', 'Hennepin Lib(OBS)', 'Hennepin County Library(OBS)', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (180, 'h', '1', 'Hennepin OBS', 'Hennepin County Library OBS', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (180, 'h', '1', 'Hennepin Lib (OBS)', 'Hennepin County Library (OBSOLETE)', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (180, 'h', '1', 'Hennepin Lib (OBS)', 'Hennepin County Library (OBSOLETE)', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (70, 'l', '1', 'LC', 'Library of Congress', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (70, 'l', '1', 'LC', 'Library of Congress', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (70, 'l', '1', 'LC', 'Library of Congress', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (70, 'l', '0', 'LC', 'Library of Congress', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (190, 's', '1', 'Agncy Sears Lst(OBS)', 'Agency for Sears List of Sbjct Hdgs (OBSOLETE)', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (190, 's', '1', 'Agncy Sears Lst(OBS)', 'Agency for Sears List of Sbjct Hdgs (OBSOLETE)', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (190, 's', '1', 'Elenco ag. Sears', 'Agenzia per lista Sears di intestaz. per sogg', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (190, 's', '1', 'Agency Sears OBS', 'Agency for Sears List of SH OBS', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (110, 'u', '1', 'Inconnu', 'Inconnu', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (110, 'u', '1', 'Sconosciuto', 'Sconosciuto', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (110, 'u', '0', 'Desconocida', 'Desconocida', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (110, 'u', '1', 'Unknown', 'Unknown', 'eng');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (200, 'v', '1', 'Univ. Laval (OBS)', 'Université Laval (OBSOLETE)', 'fra');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (200, 'v', '1', 'Univ. Laval(OBS)', 'Università Laval(OBS)', 'ita');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (200, 'v', '1', 'Univ Laval OBS', 'Université Laval OBS', 'spa');
Insert into Olisuite.T_AUT_CTLGG_SRC  Values  (200, 'v', '1', 'Univ. Laval (OBS)', 'Université Laval (OBSOLETE)', 'eng');
COMMIT;


--
-- Name: t_aut_ctlgg_src t_aut_ctlgg_src_pk; Type: CONSTRAINT; Schema: olisuite; Owner: -
--

ALTER TABLE ONLY olisuite.t_aut_ctlgg_src
    ADD CONSTRAINT t_aut_ctlgg_src_pk PRIMARY KEY (tbl_vlu_cde, langid);

--
-- Name: t_aut_ctlgg_src_tbl_vlu_cde_langid_idx; Type: INDEX; Schema: olisuite; Owner: -
--

CREATE UNIQUE INDEX t_aut_ctlgg_src_tbl_vlu_cde_langid_idx ON olisuite.t_aut_ctlgg_src USING btree (tbl_vlu_cde, langid);

--
-- Name: TABLE t_aut_ctlgg_src; Type: ACL; Schema: olisuite; Owner: -
--

--GRANT ALL ON TABLE olisuite.t_aut_ctlgg_src TO :user_name;
