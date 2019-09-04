-- MARCCAT DB         : v. 1.1
-- MARCCAT DB PLPGSQL : v. 3.1


SET client_encoding = 'UTF8';
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;


--
-- Name: amicus; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA amicus;


ALTER SCHEMA amicus OWNER TO marccat;

--
-- Name: olisuite; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA olisuite;


ALTER SCHEMA olisuite OWNER TO marccat;

--
-- Name: pack_bib_cache_punctuation; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA pack_bib_cache_punctuation;


ALTER SCHEMA pack_bib_cache_punctuation OWNER TO marccat;

--
-- Name: pack_hdg_dte; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA pack_hdg_dte;


ALTER SCHEMA pack_hdg_dte OWNER TO marccat;

--
-- Name: util; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA util;


ALTER SCHEMA util OWNER TO marccat;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: btree_gin; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS btree_gin WITH SCHEMA public;


--
-- Name: EXTENSION btree_gin; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION btree_gin IS 'support for indexing common datatypes in GIN';


--
-- Name: subfield_rec; Type: TYPE; Schema: pack_bib_cache_punctuation; Owner: -
--

CREATE TYPE pack_bib_cache_punctuation.subfield_rec AS (
	code character(1),
	text character varying(4096)
);


ALTER TYPE pack_bib_cache_punctuation.subfield_rec OWNER TO marccat;

--
-- Name: aw_aut_index(bigint, text[], boolean, boolean, boolean); Type: FUNCTION; Schema: amicus; Owner: -
--

CREATE FUNCTION amicus.aw_aut_index(rec_id bigint, ft_zones text[], ignore_xref boolean DEFAULT false, nme_ignore_lang boolean DEFAULT NULL::boolean, ttl_ignore_lang boolean DEFAULT NULL::boolean) RETURNS void
    LANGUAGE plpgsql
    AS $$

DECLARE
  FT_ZONE_ALL text := 'ALL';
  FT_ZONE_01 text := 'NME';
  FT_ZONE_02 text := 'TTL';
  FT_ZONE_03 text := 'SBJCT';
  FT_ZONE_04 text := 'NTE';

  v_usr_vw_cde bigint;
  buf_nme text;
  buf_ttl text;
  buf_sbjct text;
  buf_nte text;
  tmp_buf text;
  v_tbl_shrt_eng_txt text;
  v_tbl_shrt_eng_txt2 text;
  lang_cnt int := 0;
  lang_tmp text;
  v_bib_nte_nbr bigint;
  v_pg_ignore_ft_lang text := '0'; -- '0' indexing with language, '1' indexing without language
  v_pg_ignore_ft_nme_lang text;
  v_pg_ignore_ft_ttl_lang text;
  v_zone_01 boolean := false;
  v_zone_02 boolean := false;
  v_zone_03 boolean := false;
  v_zone_04 boolean := false;
  curr_zone text;
  i int;
  v_upd_ft_set_clause text;
  v_upd_ft_stmt text;
  v_pg_ignore_ft_aut_xref text := '0'; -- '0' indexing including xrefs, '1' indexing without including xrefs

  bib_cur cursor for
    SELECT usr_vw_cde
   	from ft_aut_data
     where aut_nbr = rec_id;

  nme_cur cursor (ign_xref text) for
    SELECT DISTINCT remove_subfield(t.nme_hdg_strng_txt), c.tbl_shrt_eng_txt
    FROM aut a, nme_hdg t, t_lang b, t_lang_of_idxg c
    WHERE t.nme_hdg_nbr = a.hdg_nbr
      AND c.tbl_vlu_cde = t.lang_of_idxg_cde
      AND a.aut_nbr = rec_id
      AND coalesce(t.NME_HDG_LANG_SCRPT_CDE, '   ') = b.tbl_vlu_cde
      AND remove_subfield(t.nme_hdg_strng_txt) IS NOT NULL
      AND a.hdg_typ_cde = 'NH'
    UNION -- Cross References
    SELECT DISTINCT remove_subfield(tx.nme_hdg_strng_txt), c.tbl_shrt_eng_txt
    FROM aut a, nme_hdg t, t_lang b, t_lang_of_idxg c, nme_ref x, nme_hdg tx
    WHERE t.nme_hdg_nbr = a.hdg_nbr
      AND c.tbl_vlu_cde = tx.lang_of_idxg_cde
      AND a.aut_nbr = rec_id
      AND coalesce(lower(tx.NME_HDG_LANG_SCRPT_CDE), '   ') = b.tbl_vlu_cde
      AND remove_subfield(tx.nme_hdg_strng_txt) IS NOT NULL
	  AND x.src_nme_hdg_nbr = a.hdg_nbr AND x.trgt_nme_hdg_nbr = tx.nme_hdg_nbr
      AND a.hdg_typ_cde = 'NH'
      AND ign_xref = '0';

  ttl_cur cursor (ign_xref text) for
    SELECT DISTINCT remove_subfield(t.ttl_hdg_strng_txt), c.tbl_shrt_eng_txt
    FROM aut a, ttl_hdg t, t_lang b, t_lang_of_idxg c
    WHERE t.ttl_hdg_nbr = a.hdg_nbr
      AND c.tbl_vlu_cde = t.lang_of_idxg_cde
      AND a.aut_nbr = rec_id
  	  AND coalesce(t.TTL_HDG_LANG_SCRPT_CDE,'   ') = b.tbl_vlu_cde
	  AND remove_subfield(t.ttl_hdg_strng_txt) IS NOT NULL
	  AND a.hdg_typ_cde = 'TH'
	UNION -- Cross References
	SELECT DISTINCT remove_subfield(tx.ttl_hdg_strng_txt), c.tbl_shrt_eng_txt
    FROM aut a, ttl_hdg t, t_lang b, t_lang_of_idxg c, ttl_ref x, ttl_hdg tx
    WHERE t.ttl_hdg_nbr = a.hdg_nbr
      AND c.tbl_vlu_cde = tx.lang_of_idxg_cde
      AND a.aut_nbr = rec_id
      AND coalesce(lower(tx.TTL_HDG_LANG_SCRPT_CDE), '   ') = b.tbl_vlu_cde
      AND remove_subfield(tx.ttl_hdg_strng_txt) IS NOT NULL
	  AND x.src_ttl_hdg_nbr = a.hdg_nbr AND x.trgt_ttl_hdg_nbr = tx.ttl_hdg_nbr
      AND a.hdg_typ_cde = 'TH'
      AND ign_xref = '0';

  sbjct_cur cursor (ign_xref text) for -- Subject has no language of indexing
    SELECT DISTINCT remove_subfield(t.sbjct_hdg_strng_txt), null
    FROM aut a, sbjct_hdg t, t_lang b
    WHERE t.sbjct_hdg_nbr = a.hdg_nbr
      AND a.aut_nbr = rec_id
	  AND coalesce(t.SBJCT_HDG_LANG_SCRPT_CDE,'   ') = b.tbl_vlu_cde
	  AND remove_subfield(t.sbjct_hdg_strng_txt) IS NOT NULL
	  AND a.hdg_typ_cde = 'SH'
	UNION -- Cross References
	SELECT DISTINCT remove_subfield(tx.sbjct_hdg_strng_txt), null
    FROM aut a, sbjct_hdg t, t_lang b, sbjct_ref x, sbjct_hdg tx
    WHERE t.sbjct_hdg_nbr = a.hdg_nbr
      AND a.aut_nbr = rec_id
      AND coalesce(lower(tx.SBJCT_HDG_LANG_SCRPT_CDE), '   ') = b.tbl_vlu_cde
      AND remove_subfield(tx.sbjct_hdg_strng_txt) IS NOT NULL
	  AND x.src_sbjct_hdg_nbr = a.hdg_nbr AND x.trgt_sbjct_hdg_nbr = tx.sbjct_hdg_nbr
      AND a.hdg_typ_cde = 'SH'
      AND ign_xref = '0';

  nte_cur cursor for -- Note has no language of indexing
    SELECT DISTINCT remove_subfield(a.AUT_NTE_STRNG_TXT), null
    FROM aut_nte a, t_lang b
    WHERE a.aut_nbr = rec_id
	  AND coalesce(a.AUT_NTE_LANG_SCRPT_CDE,'   ') = b.tbl_vlu_cde
	  AND remove_subfield(a.AUT_NTE_STRNG_TXT) IS NOT NULL;
BEGIN
  -- load configuration
  select GLBL_VRBL_VLU into v_pg_ignore_ft_nme_lang from S_SYS_GLBL_VRBL where GLBL_VRBL_NME = 'pg_ignore_ft_aut_nme_lang';
  select GLBL_VRBL_VLU into v_pg_ignore_ft_ttl_lang from S_SYS_GLBL_VRBL where GLBL_VRBL_NME = 'pg_ignore_ft_aut_ttl_lang';
  select GLBL_VRBL_VLU into v_pg_ignore_ft_aut_xref from S_SYS_GLBL_VRBL where GLBL_VRBL_NME = 'pg_ignore_ft_aut_xref';

  -- Override configuration read from S_SYS_GLBL_VRBL ------------------------------
  if ignore_xref is not null and ignore_xref
  then
    v_pg_ignore_ft_aut_xref := '1';
   end if;

   if ignore_xref is not null and not ignore_xref
   then
     v_pg_ignore_ft_aut_xref := '0';
   end if;
   -- -------------------------------------------------------------------------------

  -- initialize zone checks
  if ft_zones is not null
  then
    i := 0;
    FOREACH curr_zone IN ARRAY ft_zones
    LOOP
      i := i+1;
      if upper(ft_zones[i]) = FT_ZONE_ALL
      then
        v_zone_01 := true;
        v_zone_02 := true;
        v_zone_03 := true;
        v_zone_04 := true;
        exit;
      end if;
      if upper(ft_zones[i]) = FT_ZONE_01
      then
        v_zone_01 := true;
        continue;
      end if;
      if upper(ft_zones[i]) = FT_ZONE_02
      then
        v_zone_02 := true;
        continue;
      end if;
      if upper(ft_zones[i]) = FT_ZONE_03
      then
        v_zone_03 := true;
        continue;
      end if;
      if upper(ft_zones[i]) = FT_ZONE_04
      then
        v_zone_04 := true;
        continue;
      end if;
    END LOOP;
  end if;

  -- Refresh FT_DATA
  begin
    if v_zone_01
    then
      update FT_AUT_DATA set pg_ft_nme = null, pg_ft_nme_lang = null where aut_nbr = rec_id;
    end if;
    if v_zone_02
    then
      update FT_AUT_DATA set pg_ft_ttl = null, pg_ft_ttl_lang = null where aut_nbr = rec_id;
    end if;
    if v_zone_03
    then
      update FT_AUT_DATA set pg_ft_sbjct = null, pg_ft_sbjct_lang = null where aut_nbr = rec_id;
    end if;
    if v_zone_04
    then
      update FT_AUT_DATA set pg_ft_nte = null, pg_ft_nte_lang = null where aut_nbr = rec_id;
    end if;

    insert into FT_AUT_DATA(aut_nbr, usr_vw_cde)
    (select a.aut_nbr, -1
     from AUT a
     where a.aut_nbr = rec_id
       and not exists (select 1
                       from ft_aut_data b
                       where b.aut_nbr = a.aut_nbr));
  exception
  when others
  then
    null;
  end;

  open bib_cur;
  loop
    fetch bib_cur into v_usr_vw_cde;
    exit WHEN NOT FOUND;

    v_upd_ft_set_clause := null;
    v_upd_ft_stmt := null;

    -- Start Names section ---------------------------------------------------------------
    if v_zone_01
    then
		open nme_cur(v_pg_ignore_ft_aut_xref);
		lang_cnt := 0;
		lang_tmp := null;
		tmp_buf := null;
		buf_nme := null;
		v_pg_ignore_ft_lang := v_pg_ignore_ft_nme_lang;
		loop
		  fetch nme_cur into tmp_buf, v_tbl_shrt_eng_txt;
		  exit WHEN NOT FOUND;
		  if coalesce(nullif(v_tbl_shrt_eng_txt, ''), '') <> coalesce(nullif(lang_tmp, ''), '')
		  then
			lang_cnt := lang_cnt + 1;
		  end if;
		  lang_tmp := v_tbl_shrt_eng_txt;
		  buf_nme := coalesce(buf_nme, '') || ' ' || coalesce(tmp_buf, '');
		end loop;
		close nme_cur;

	    buf_nme := replace(buf_nme, chr(39), chr(39)||chr(39));
		v_tbl_shrt_eng_txt := lower(trim(lang_tmp));

		-- Override configuration read from S_SYS_GLBL_VRBL ----------------------------------
		if nme_ignore_lang is not null and nme_ignore_lang
		then
		  v_pg_ignore_ft_lang := '1';
		end if;

		if nme_ignore_lang is not null and not nme_ignore_lang
		then
		  v_pg_ignore_ft_lang := '0';
		end if;
		-- -----------------------------------------------------------------------------------

		if coalesce(nullif(trim(v_pg_ignore_ft_lang), '0'), '0') in ('0', '')
		then
			if (lang_cnt = 1)
			then
			  if lower(trim(v_tbl_shrt_eng_txt)) not in (SELECT cfgname FROM pg_ts_config) -- check if language is supported
			  then
				if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
				then
                  v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
				end if;
				v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
				                       'PG_FT_NME = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_nme, '') || '''' ||
				                       '), PG_FT_NME_LANG = ''simple''::regconfig';
			  else
				if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
				then
                  v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
				end if;
				v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
				                       'PG_FT_NME = to_tsvector(v_tbl_shrt_eng_txt::regconfig, ''' ||  coalesce(buf_nme, '') || '''' ||
				                       '), PG_FT_NME_LANG = v_tbl_shrt_eng_txt';
			  end if;
			else
			  if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
			  then
                v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
			  end if;
			  v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
			                         'PG_FT_NME = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_nme, '') || '''' ||
			                         '), PG_FT_NME_LANG = ''simple''::regconfig';
			end if;
		else -- ignore language while indexing
		  if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
		  then
            v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
		  end if;
		  v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
			                     'PG_FT_NME = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_nme, '') || '''' ||
			                     '), PG_FT_NME_LANG = ''simple''::regconfig';
		end if;
    end if;
    -- End Names section -----------------------------------------------------------------

    -- Start Titles section --------------------------------------------------------------
    if v_zone_02
    then
		open ttl_cur(v_pg_ignore_ft_aut_xref);
		lang_cnt := 0;
		lang_tmp := null;
		tmp_buf := null;
		buf_ttl := null;
		v_pg_ignore_ft_lang := v_pg_ignore_ft_nme_lang;
		loop
		  fetch ttl_cur into tmp_buf, v_tbl_shrt_eng_txt;
		  exit WHEN NOT FOUND;
		  if coalesce(nullif(v_tbl_shrt_eng_txt, ''), '') <> coalesce(nullif(lang_tmp, ''), '')
		  then
			lang_cnt := lang_cnt + 1;
		  end if;
		  lang_tmp := v_tbl_shrt_eng_txt;
		  buf_ttl := coalesce(buf_ttl, '') || ' ' || coalesce(tmp_buf, '');
		end loop;
		close ttl_cur;

	    buf_ttl := replace(buf_ttl, chr(39), chr(39)||chr(39));
		v_tbl_shrt_eng_txt := lower(trim(lang_tmp));

		-- Override configuration read from S_SYS_GLBL_VRBL ----------------------------------
		if ttl_ignore_lang is not null and ttl_ignore_lang
		then
		  v_pg_ignore_ft_lang := '1';
		end if;

		if ttl_ignore_lang is not null and not ttl_ignore_lang
		then
		  v_pg_ignore_ft_lang := '0';
		end if;
		-- -----------------------------------------------------------------------------------

		if coalesce(nullif(trim(v_pg_ignore_ft_lang), '0'), '0') in ('0', '')
		then
			if (lang_cnt = 1)
			then
			  if lower(trim(v_tbl_shrt_eng_txt)) not in (SELECT cfgname FROM pg_ts_config) -- check if language is supported
			  then
				if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
				then
                  v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
				end if;
				v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
				                       'PG_FT_TTL = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_ttl, '') || '''' ||
				                       '), PG_FT_TTL_LANG = ''simple''::regconfig';
			  else
				if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
				then
                  v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
				end if;
				v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
				                       'PG_FT_TTL = to_tsvector(v_tbl_shrt_eng_txt::regconfig, ''' ||  coalesce(buf_ttl, '') || '''' ||
				                       '), PG_FT_TTL_LANG = v_tbl_shrt_eng_txt';
			  end if;
			else
			  if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
			  then
                v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
			  end if;
			  v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
			                         'PG_FT_TTL = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_ttl, '') || '''' ||
			                         '), PG_FT_TTL_LANG = ''simple''::regconfig';
			end if;
		else -- ignore language while indexing
		  if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
		  then
            v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
		  end if;
		  v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
			                     'PG_FT_TTL = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_ttl, '') || '''' ||
			                     '), PG_FT_TTL_LANG = ''simple''::regconfig';
		end if;
    end if;
    -- End Titles section ----------------------------------------------------------------

    -- Start Subjects section ------------------------------------------------------------
    if v_zone_03
    then
		open sbjct_cur(v_pg_ignore_ft_aut_xref);
		lang_cnt := 0;
		lang_tmp := null;
		tmp_buf := null;
		buf_sbjct := null;
		v_pg_ignore_ft_lang := '1'; -- language not supported for this zone
		loop
		  fetch sbjct_cur into tmp_buf, v_tbl_shrt_eng_txt;
		  exit WHEN NOT FOUND;
		  if coalesce(nullif(v_tbl_shrt_eng_txt, ''), '') <> coalesce(nullif(lang_tmp, ''), '')
		  then
			lang_cnt := lang_cnt + 1;
		  end if;
		  lang_tmp := v_tbl_shrt_eng_txt;
		  buf_sbjct := coalesce(buf_sbjct, '') || ' ' || coalesce(tmp_buf, '');
		end loop;
		close sbjct_cur;

	    buf_sbjct := replace(buf_sbjct, chr(39), chr(39)||chr(39));
		v_tbl_shrt_eng_txt := lower(trim(lang_tmp));

		if coalesce(nullif(trim(v_pg_ignore_ft_lang), '0'), '0') in ('0', '')
		then
			if (lang_cnt = 1)
			then
			  if lower(trim(v_tbl_shrt_eng_txt)) not in (SELECT cfgname FROM pg_ts_config) -- check if language is supported
			  then
				if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
				then
                  v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
				end if;
				v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
				                       'PG_FT_SBJCT = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_sbjct, '') || '''' ||
				                       '), PG_FT_SBJCT_LANG = ''simple''::regconfig';
			  else
				if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
				then
                  v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
				end if;
				v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
				                       'PG_FT_SBJCT = to_tsvector(v_tbl_shrt_eng_txt::regconfig, ''' ||  coalesce(buf_sbjct, '') || '''' ||
				                       '), PG_FT_SBJCT_LANG = v_tbl_shrt_eng_txt';
			  end if;
			else
			  if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
			  then
                v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
			  end if;
			  v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
			                         'PG_FT_SBJCT = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_sbjct, '') || '''' ||
			                         '), PG_FT_SBJCT_LANG = ''simple''::regconfig';
			end if;
		else -- ignore language while indexing
		  if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
		  then
            v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
		  end if;
		  v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
			                     'PG_FT_SBJCT = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_sbjct, '') || '''' ||
			                     '), PG_FT_SBJCT_LANG = ''simple''::regconfig';
		end if;
    end if;
    -- End Subjects section --------------------------------------------------------------

    -- Start Notes section ---------------------------------------------------------------
    if v_zone_04
    then
		open nte_cur;
		lang_cnt := 0;
		lang_tmp := null;
		tmp_buf := null;
		buf_nte := null;
		v_pg_ignore_ft_lang := '1'; -- language not supported for this zone
		loop
		  fetch nte_cur into tmp_buf, v_tbl_shrt_eng_txt;
		  exit WHEN NOT FOUND;
		  if coalesce(nullif(v_tbl_shrt_eng_txt, ''), '') <> coalesce(nullif(lang_tmp, ''), '')
		  then
			lang_cnt := lang_cnt + 1;
		  end if;
		  lang_tmp := v_tbl_shrt_eng_txt;
		  buf_nte := coalesce(buf_nte, '') || ' ' || coalesce(tmp_buf, '');
		end loop;
		close nte_cur;

	    buf_nte := replace(buf_nte, chr(39), chr(39)||chr(39));
		v_tbl_shrt_eng_txt := lower(trim(lang_tmp));

		if coalesce(nullif(trim(v_pg_ignore_ft_lang), '0'), '0') in ('0', '')
		then
			if (lang_cnt = 1)
			then
			  if lower(trim(v_tbl_shrt_eng_txt)) not in (SELECT cfgname FROM pg_ts_config) -- check if language is supported
			  then
				if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
				then
                  v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
				end if;
				v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
				                       'PG_FT_NTE = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_nte, '') || '''' ||
				                       '), PG_FT_NTE_LANG = ''simple''::regconfig';
			  else
				if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
				then
                  v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
				end if;
				v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
				                       'PG_FT_NTE = to_tsvector(v_tbl_shrt_eng_txt::regconfig, ''' ||  coalesce(buf_nte, '') || '''' ||
				                       '), PG_FT_NTE_LANG = v_tbl_shrt_eng_txt';
			  end if;
			else
			  if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
			  then
                v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
			  end if;
			  v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
			                         'PG_FT_NTE = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_nte, '') || '''' ||
			                         '), PG_FT_NTE_LANG = ''simple''::regconfig';
			end if;
		else -- ignore language while indexing
		  if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
		  then
            v_upd_ft_set_clause := v_upd_ft_set_clause || ', ';
		  end if;
		  v_upd_ft_set_clause := coalesce(v_upd_ft_set_clause, '') ||
			                     'PG_FT_NTE = to_tsvector(''simple''::regconfig, ''' || coalesce(buf_nte, '') || '''' ||
			                     '), PG_FT_NTE_LANG = ''simple''::regconfig';
		end if;
    end if;
    -- End Notes section -----------------------------------------------------------------


    if not (v_upd_ft_set_clause is null or v_upd_ft_set_clause = '')
    then
      v_upd_ft_stmt := 'UPDATE FT_AUT_DATA SET ' || v_upd_ft_set_clause ||
                       ' WHERE aut_nbr = ' || rec_id || ' and usr_vw_cde = ' || v_usr_vw_cde || ';';
    end if;

    if not (v_upd_ft_stmt is null or v_upd_ft_stmt = '')
    then
      execute v_upd_ft_stmt;

      -- Clean up FT_AUT_UPDATE
      delete from FT_AUT_UPDATE
      where aut_nbr = rec_id;
    end if;
  end loop;
  close bib_cur;
end;

$$;