package librisuite.business.cataloguing.common;


public class ConstantSqlRelation {

	public static final String SELECT_WRK_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_RLT_TYP WHERE WRK_RLT_CODE=?";
	public static final String SELECT_WRK_SUB_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_SUB_RLT_TYP WHERE WRK_RLT_SUB_CODE=?";
	
	public static final String SELECT_EXP_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_EXP_RLT_TYP WHERE EXP_RLT_CODE=?";
	public static final String SELECT_EXP_SUB_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_EXP_SUB_RLT_TYP WHERE EXP_RLT_SUB_CODE=?";

	public static final String SELECT_MNF_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_MNF_RLT_TYP WHERE MNF_RLT_CODE=?";
	public static final String SELECT_MNF_SUB_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_MNF_SUB_RLT_TYP WHERE MNF_RLT_SUB_CODE=?";
	
	public static final String SELECT_ITM_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_ITM_RLT_TYP WHERE ITM_RLT_CODE=?";
	public static final String SELECT_ITM_SUB_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_ITM_SUB_RLT_TYP WHERE ITM_RLT_SUB_CODE=?";

	public static final String SELECT_FML_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_FML_RLT_TYP WHERE FML_RLT_CODE=?";
	public static final String SELECT_FML_SUB_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_FML_SUB_RLT_TYP WHERE FML_RLT_SUB_CODE=?";
	
	public static final String SELECT_PRS_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_PRS_RLT_TYP WHERE PRS_RLT_CODE=?";
	public static final String SELECT_PRS_SUB_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_PRS_SUB_RLT_TYP WHERE PRS_RLT_SUB_CODE=?";
	
	public static final String SELECT_CRB_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_CRB_RLT_TYP WHERE CRB_RLT_CODE=?";
	public static final String SELECT_CRB_SUB_RLT_TYPE= "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_CRB_SUB_RLT_TYP WHERE CRB_RLT_SUB_CODE=?";

	public static final String SELECT_DESIGNATORS = "SELECT TAG_MARC_AUTH_SEQ,TAG_MARC_BIB_SEQ FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_DESIGNATORS WHERE ID=?";
	public static final String SELECT_SUB_DESIGNATORS = "";
	public static final String SELECT_SUB_SUB_DESIGNATORS = "";
	
	
	
	public static String SELECT_BIBLIOGRAPHIC_MANIFESTATION = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL a LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT b ON a.bib_itm_nbr=b.b_bib_itm_nbr WHERE RLT_CODE=? AND (a.BIB_ITM_NBR    =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE b_bib_itm_nbr =a.BIB_ITM_NBR_rel) = 5) OR (a.BIB_ITM_NBR_REL =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE b_bib_itm_nbr=a.BIB_ITM_NBR) = 5)";
	public static String SELECT_AUTHORITY_MANIFESTATION = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL a LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT b ON a.bib_itm_nbr=b.b_bib_itm_nbr WHERE RLT_CODE=? AND (a.BIB_ITM_NBR    =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE a_bib_itm_nbr =a.BIB_ITM_NBR_rel) = 5) OR (a.BIB_ITM_NBR_REL =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE a_bib_itm_nbr=a.BIB_ITM_NBR) = 5)";
	
	
	public static String SELECT_BIBLIOGRAPHIC_EXPRESSION = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL a LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT b ON a.bib_itm_nbr=b.b_bib_itm_nbr WHERE RLT_CODE=? AND (a.BIB_ITM_NBR    =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE b_bib_itm_nbr =a.BIB_ITM_NBR_rel) = 4) OR (a.BIB_ITM_NBR_REL =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE b_bib_itm_nbr=a.BIB_ITM_NBR) = 4)";
	public static String SELECT_AUTHORITY_EXPRESSION = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL a LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT b ON a.bib_itm_nbr=b.b_bib_itm_nbr WHERE RLT_CODE=? AND (a.BIB_ITM_NBR    =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE a_bib_itm_nbr =a.BIB_ITM_NBR_rel) = 2) OR (a.BIB_ITM_NBR_REL =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE a_bib_itm_nbr=a.BIB_ITM_NBR) = 2)";
	
	public static String SELECT_BIBLIOGRAPHIC_WORK = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL a LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT b ON a.bib_itm_nbr=b.b_bib_itm_nbr WHERE RLT_CODE=? AND (a.BIB_ITM_NBR    =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE b_bib_itm_nbr =a.BIB_ITM_NBR_rel) = 3) OR (a.BIB_ITM_NBR_REL =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE b_bib_itm_nbr=a.BIB_ITM_NBR) = 3)";
	public static String SELECT_AUTHORITY_WORK = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL a LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT b ON a.bib_itm_nbr=b.b_bib_itm_nbr WHERE RLT_CODE=? AND (a.BIB_ITM_NBR    =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE a_bib_itm_nbr =a.BIB_ITM_NBR_rel) = 1) OR (a.BIB_ITM_NBR_REL =? AND (SELECT frbr_hdg_typ_cde FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT WHERE a_bib_itm_nbr=a.BIB_ITM_NBR) = 1)";
	
	
	
	public static String SELECT_HEADING_RELATION = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_HDG_REL A LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".FRBR_ACS_PNT B ON A.BIB_ITM_NBR=B.B_BIB_ITM_NBR WHERE RLT_CODE=? AND A.BIB_ITM_NBR=? ";
	
	//public static String SELECT_SUMMARY_RECORD_RELATION = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL A WHERE RLT_CODE=? AND (A.BIB_ITM_NBR=? OR A.BIB_ITM_NBR_REL=?)";
	public static String SELECT_SUMMARY_RECORD_RELATION = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_RECORD_REL A WHERE RLT_CODE=? AND A.BIB_ITM_NBR=?";
	public static String SELECT_SUMMARY_HEADING_RELATION = "SELECT * FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_HDG_REL A WHERE RLT_CODE=? AND A.BIB_ITM_NBR=? ";
	
	/*public static String SELECT_WORK_RELATION_TYPES = "SELECT DISTINCT A.LVL_RLT_TYPE,A.WRK_RLT_NAME,B.LVL_RLT_NAME,A.WRK_RLT_CODE,c.WRK_RLT_SUB_CODE,c.WRK_RLT_SUB_NAME " +
														"FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_RLT_TYP A " +
														"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " +
														"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_SUB_RLT_TYP c on  a.wrk_rlt_code=c.wrk_rlt_code";*/
	
	public static String SELECT_WORK_RELATION_TYPES = "SELECT DISTINCT A.LVL_RLT_TYPE,A.WRK_RLT_NAME,B.LVL_RLT_NAME,A.WRK_RLT_CODE,c.WRK_RLT_SUB_CODE,c.WRK_RLT_SUB_NAME " +
	"FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_RLT_TYP A " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_SUB_RLT_TYP c on  a.wrk_rlt_code=c.wrk_rlt_code";
	
	public static String SELECT_EXPRESSION_RELATION_TYPES = "SELECT DISTINCT A.LVL_RLT_TYPE,A.EXP_RLT_NAME,B.LVL_RLT_NAME,A.EXP_RLT_CODE,c.EXP_RLT_SUB_CODE,c.EXP_RLT_SUB_NAME " +
	"FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_EXP_RLT_TYP A " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_EXP_SUB_RLT_TYP c on  a.exp_rlt_code=c.exp_rlt_code";
	
	public static String SELECT_MANIFESTATION_RELATION_TYPES = "SELECT DISTINCT A.LVL_RLT_TYPE,A.MNF_RLT_NAME,B.LVL_RLT_NAME,A.MNF_RLT_CODE,c.MNF_RLT_SUB_CODE,c.MNF_RLT_SUB_NAME " +
	"FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_MNF_RLT_TYP A " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_MNF_SUB_RLT_TYP c on  a.mnf_rlt_code=c.mnf_rlt_code";
	
	public static String SELECT_PERSON_RELATION_TYPES = "SELECT DISTINCT A.LVL_RLT_TYPE,A.PRS_RLT_NAME,B.LVL_RLT_NAME,A.PRS_RLT_CODE,c.PRS_RLT_SUB_CODE,c.PRS_RLT_SUB_NAME " +
	"FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_PRS_RLT_TYP A " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_PRS_SUB_RLT_TYP c on  a.prs_rlt_code=c.prs_rlt_code";
	
	public static String SELECT_ITEM_RELATION_TYPES = "SELECT DISTINCT A.LVL_RLT_TYPE,A.ITM_RLT_NAME,B.LVL_RLT_NAME,A.ITM_RLT_CODE,c.ITM_RLT_SUB_CODE,c.ITM_RLT_SUB_NAME " +
	"FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_ITM_RLT_TYP A " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_ITM_SUB_RLT_TYP c on  a.itm_rlt_code=c.itm_rlt_code";
	
	public static String SELECT_CORPORATE_BODY_RELATION_TYPES = "SELECT DISTINCT A.LVL_RLT_TYPE,A.CRB_RLT_NAME,B.LVL_RLT_NAME,A.CRB_RLT_CODE,c.CRB_RLT_SUB_CODE,c.CRB_RLT_SUB_NAME " +
	"FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_CRB_RLT_TYP A " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_CRB_SUB_RLT_TYP c on  a.crb_rlt_code=c.crb_rlt_code";
	
	public static String SELECT_FAMILY_RELATION_TYPES = "SELECT DISTINCT A.LVL_RLT_TYPE,A.FML_RLT_NAME,B.LVL_RLT_NAME,A.FML_RLT_CODE,c.FML_RLT_SUB_CODE,c.FML_RLT_SUB_NAME " +
	"FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_FML_RLT_TYP A " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=B.LVL_RLT_TYPE " +
	"LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_FML_SUB_RLT_TYP c on  a.fml_rlt_code=c.fml_rlt_code";
	
	
	public static String SELECT_PRIMARY_WORK_RELATIONS = "SELECT LVL_RLT_NAME,WRK_RLT_CODE,WRK_RLT_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_WRK_RLT_TYP A LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=A.LVL_RLT_TYPE WHERE A.LVL_RLT_TYPE = 'P_00' AND B.LVL_RLT_TYPE='P_00'";
	public static String SELECT_PRIMARY_EXPRESSION_RELATIONS = "SELECT LVL_RLT_NAME,EXP_RLT_CODE,EXP_RLT_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_EXP_RLT_TYP A LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=A.LVL_RLT_TYPE WHERE A.LVL_RLT_TYPE = 'P_00' AND B.LVL_RLT_TYPE='P_00'";
	public static String SELECT_PRIMARY_MANIFESTATION_RELATIONS = 	"SELECT LVL_RLT_NAME,MNF_RLT_CODE,MNF_RLT_NAME FROM " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_MNF_RLT_TYP A LEFT OUTER JOIN " + System.getProperty(com.atc.weloan.shared.Global.SCHEMA_SUITE_KEY) + ".F_LVL_RLT B ON A.LVL_RLT_TYPE=A.LVL_RLT_TYPE WHERE A.LVL_RLT_TYPE = 'P_00' AND B.LVL_RLT_TYPE='P_00'";

}
