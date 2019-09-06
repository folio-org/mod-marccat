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
-- Name: s_patch_history; Type: TABLE; Schema: amicus; Owner: -
--

CREATE TABLE amicus.s_patch_history (
    apply_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    cr_number integer NOT NULL,
    cr_part character(4) DEFAULT ' '::bpchar NOT NULL,
    release_number real NOT NULL,
    service_pack_number smallint DEFAULT 0 NOT NULL
);


ALTER TABLE amicus.s_patch_history OWNER TO marccat;

--
-- Name: TABLE s_patch_history; Type: COMMENT; Schema: amicus; Owner: -
--

COMMENT ON TABLE amicus.s_patch_history IS 'Contains information about the installed base patches of the Amicus component.';


--
-- Data for Name: s_patch_history; Type: TABLE DATA; Schema: amicus; Owner: -
--

COPY amicus.s_patch_history (apply_date, cr_number, cr_part, release_number, service_pack_number) FROM stdin;
2018-12-05 17:13:15	3066	    	3.5	4
2018-12-05 17:13:16	3067	c   	3.5	4
2018-12-05 17:13:16	3067	d   	3.5	4
2018-12-05 17:13:16	3067	e   	3.5	4
2018-12-05 17:13:16	3070	    	3.5	4
2018-12-05 17:13:16	3071	    	3.5	4
2018-12-05 17:13:16	3072	    	3.5	4
2018-12-05 17:13:17	414	    	3.5	4
2013-07-11 11:26:58	2746	    	3.5	3
2013-07-11 11:26:58	2749	    	3.5	3
2013-07-11 11:26:59	2757	    	3.5	3
2013-07-11 11:26:59	2761	    	3.5	3
2013-07-11 11:27:00	2768	    	3.5	3
2013-07-11 11:27:00	2745	    	3.5	3
2013-07-11 11:30:35	2740	a   	3.5	3
2013-07-11 11:30:40	2740	b   	3.5	3
2013-07-11 11:30:40	2772	    	3.5	3
2013-07-11 11:30:40	2773	    	3.5	3
2013-07-11 11:30:40	2777	    	3.5	3
2013-07-11 11:30:40	2783	    	3.5	3
2013-07-11 11:30:40	2788	    	3.5	3
2013-07-11 11:30:41	2789	    	3.5	3
2013-07-11 11:30:41	2791	a   	3.5	3
2013-07-11 11:30:41	2791	b   	3.5	3
2013-07-11 11:30:41	2797	    	3.5	3
2013-07-11 11:30:41	2798	    	3.5	3
2013-07-11 11:30:42	2802	a   	3.5	3
2013-07-11 11:30:43	2802	b   	3.5	3
2013-07-11 11:30:44	2802	c   	3.5	3
2013-07-11 11:30:44	2804	    	3.5	3
2013-07-11 11:30:44	2805	    	3.5	3
2013-07-11 11:30:44	2811	    	3.5	3
2013-07-11 11:30:44	2808	    	3.5	3
2013-07-11 11:30:44	2816	    	3.5	3
2013-07-11 11:30:44	2821	    	3.5	3
2013-07-11 11:30:44	2824	    	3.5	3
2013-07-11 11:40:52	2863	    	3.5	4
2013-07-11 11:40:52	2873	    	3.5	4
2013-07-11 11:40:52	2876	    	3.5	4
2013-07-11 11:40:52	2877	    	3.5	4
2013-07-11 11:40:52	2882	    	3.5	4
2013-07-11 11:40:52	2884	a   	3.5	4
2013-07-11 11:40:53	2884	b   	3.5	4
2013-07-11 11:40:53	2889	    	3.5	4
2013-07-11 11:40:53	2894	    	3.5	4
2013-07-11 11:40:54	2895	    	3.5	4
2013-07-11 11:40:54	2898	    	3.5	4
2013-07-11 11:40:54	2907	    	3.5	4
2013-07-11 11:40:54	2909	    	3.5	4
2013-07-11 11:40:54	2911	    	3.5	4
2013-07-11 11:40:54	2916	    	3.5	4
2013-07-11 11:40:54	2920	    	3.5	4
2013-07-11 11:40:54	2921	    	3.5	4
2013-07-11 11:40:55	2927	    	3.5	4
2013-07-11 11:40:56	2928	    	3.5	4
2013-07-11 11:40:56	2929	    	3.5	4
2013-07-11 11:40:56	2936	    	3.5	4
2013-07-11 11:40:56	2937	a   	3.5	4
2013-07-11 11:41:02	2937	b   	3.5	4
2013-07-11 11:41:02	2937	c   	3.5	4
2013-07-11 11:41:02	2938	    	3.5	4
2013-07-11 11:41:02	2943	    	3.5	4
2013-07-11 11:41:02	2946	    	3.5	4
2013-07-11 11:41:03	2947	b   	3.5	4
2013-07-11 11:41:03	2947	    	3.5	4
2013-07-11 11:41:03	2948	    	3.5	4
2013-07-11 11:41:03	2950	    	3.5	4
2013-07-11 11:41:03	2955	    	3.5	4
2018-12-05 11:39:06	2841	    	4	0
2018-12-05 11:39:58	2849	    	4	0
2018-12-05 11:39:58	2850	    	4	0
2018-12-05 11:39:58	2851	    	4	0
2018-12-05 11:39:58	2852	    	4	0
2018-12-05 11:39:58	2853	    	4	0
2013-07-11 11:44:31	2975	    	3.5	4
2013-07-11 11:44:32	2907	a   	3.5	4
2013-07-11 11:44:32	2987	    	3.5	4
2013-07-11 11:44:32	2988	    	3.5	4
2013-07-11 11:44:32	2990	    	3.5	4
2013-07-11 11:45:27	2992	    	3.5	4
2013-07-11 11:45:27	3024	    	3.5	4
2013-07-11 11:45:27	3028	    	3.5	4
2013-07-11 11:45:27	3034	    	3.5	4
2013-07-11 11:45:27	3037	    	3.5	4
2013-07-11 11:45:27	3044	    	3.5	4
2013-07-11 11:45:27	2992	a   	3.5	4
2013-07-11 11:45:27	2995	    	3.5	4
2013-07-11 11:45:27	2998	    	3.5	4
2013-07-11 11:45:27	2999	    	3.5	4
2013-07-11 11:45:27	3006	    	3.5	4
2013-07-11 11:45:27	3018	    	3.5	4
2013-07-11 11:45:27	3019	    	3.5	4
2013-07-11 11:45:27	3030	    	3.5	4
2013-07-11 11:45:27	3035	    	3.5	4
2013-07-11 11:45:27	3037	a   	3.5	4
2013-07-11 11:45:27	3048	    	3.5	4
2013-07-11 11:45:28	3052	    	3.5	4
2013-07-11 11:45:28	3055	    	3.5	4
2013-07-11 11:45:28	3057	    	3.5	4
2013-07-11 11:45:28	3057	a   	3.5	4
2013-07-11 11:46:48	3067	    	3.5	4
\.