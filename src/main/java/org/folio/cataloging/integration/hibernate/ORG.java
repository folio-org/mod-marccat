/*
 * Created on Apr 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wimc
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ORG implements Serializable {
    private int ORG_NBR;
    private short LCKG_VRSN_UPDT_NBR;
    private int FNC_CSTMR_NBR;
    private short BLNG_GRP_SEQ_NBR;
    private int NLC_RESP_CNTR_NBR;
    private int TOP_LIB_ORG_NBR;
    private int ORG_STAFF_CNT;
    private int ORG_TOTL_MBSHP_CNT;
    private float FNC_DPST_ACNT_AMT;
    private Date ORG_LST_UPDT_TMEST;
    private short GOVT_DEPT_CDE;
    private short GOVT_TYP_CDE;
    private short ORG_STUS_CDE;
    private short LANG_PRFNC_CDE;
    private short ORG_BSNS_TYP_CDE;
    private short BLNG_GRP_1_CDE;
    private String TRLTN_AGNCY_SMBL_CDE;
    private char FNC_ACNT_STNDG_IND;
    private char ORG_PRFT_NON_PRFT_IND;
    private char ORG_LIB_IND;
    private char ORG_CDN_IND;
    private char PST_EXPTN_IND;
    private char GST_EXPTN_IND;
    private char PRVCL_SERV_TAX_EXPTN_IND;
    private Date ORG_CRTN_DTE;
    private String ORG_FR_MSSN_DSC;
    private String ORG_ENG_MSSN_DSC;
    private String ORG_NTE;
    private String ORG_PFMNC_NTE;
    private String FNC_ACNT_STNDG_NTE;

    /**
     * @return BLNG_GRP_1_CDE
     */
    public short getBLNG_GRP_1_CDE() {
        return BLNG_GRP_1_CDE;
    }

    /**
     * @return BLNG_GRP_SEQ_NBR
     */
    public short getBLNG_GRP_SEQ_NBR() {
        return BLNG_GRP_SEQ_NBR;
    }

    /**
     * @return FNC_ACNT_STNDG_IND
     */
    public char getFNC_ACNT_STNDG_IND() {
        return FNC_ACNT_STNDG_IND;
    }

    /**
     * @return FNC_ACNT_STNDG_NTE
     */
    public String getFNC_ACNT_STNDG_NTE() {
        return FNC_ACNT_STNDG_NTE;
    }

    /**
     * @return FNC_CSTMR_NBR
     */
    public int getFNC_CSTMR_NBR() {
        return FNC_CSTMR_NBR;
    }

    /**
     * @return FNC_DPST_ACNT_AMT
     */
    public float getFNC_DPST_ACNT_AMT() {
        return FNC_DPST_ACNT_AMT;
    }

    /**
     * @return GOVT_DEPT_CDE
     */
    public short getGOVT_DEPT_CDE() {
        return GOVT_DEPT_CDE;
    }

    /**
     * @return GOVT_TYP_CDE
     */
    public short getGOVT_TYP_CDE() {
        return GOVT_TYP_CDE;
    }

    /**
     * @return GST_EXPTN_IND
     */
    public char getGST_EXPTN_IND() {
        return GST_EXPTN_IND;
    }

    /**
     * @return LANG_PRFNC_CDE
     */
    public short getLANG_PRFNC_CDE() {
        return LANG_PRFNC_CDE;
    }

    /**
     * @return LCKG_VRSN_UPDT_NBR
     */
    public short getLCKG_VRSN_UPDT_NBR() {
        return LCKG_VRSN_UPDT_NBR;
    }

    /**
     * @return NLC_RESP_CNTR_NBR
     */
    public int getNLC_RESP_CNTR_NBR() {
        return NLC_RESP_CNTR_NBR;
    }

    /**
     * @return ORG_BSNS_TYP_CDE
     */
    public short getORG_BSNS_TYP_CDE() {
        return ORG_BSNS_TYP_CDE;
    }

    /**
     * @return ORG_CDN_IND
     */
    public char getORG_CDN_IND() {
        return ORG_CDN_IND;
    }

    /**
     * @return ORG_CRTN_DTE
     */
    public Date getORG_CRTN_DTE() {
        return ORG_CRTN_DTE;
    }

    /**
     * @return ORG_ENG_MSSN_DSC
     */
    public String getORG_ENG_MSSN_DSC() {
        return ORG_ENG_MSSN_DSC;
    }

    /**
     * @return ORG_FR_MSSN_DSC
     */
    public String getORG_FR_MSSN_DSC() {
        return ORG_FR_MSSN_DSC;
    }

    /**
     * @return ORG_LIB_IND
     */
    public char getORG_LIB_IND() {
        return ORG_LIB_IND;
    }

    /**
     * @return ORG_LST_UPDT_TMEST
     */
    public Date getORG_LST_UPDT_TMEST() {
        return ORG_LST_UPDT_TMEST;
    }

    /**
     * @return ORG_NBR
     */
    public int getORG_NBR() {
        return ORG_NBR;
    }

    /**
     * @return ORG_NTE
     */
    public String getORG_NTE() {
        return ORG_NTE;
    }

    /**
     * @return ORG_PFMNC_NTE
     */
    public String getORG_PFMNC_NTE() {
        return ORG_PFMNC_NTE;
    }

    /**
     * @return ORG_PRFT_NON_PRFT_IND
     */
    public char getORG_PRFT_NON_PRFT_IND() {
        return ORG_PRFT_NON_PRFT_IND;
    }

    /**
     * @return ORG_STAFF_CNT
     */
    public int getORG_STAFF_CNT() {
        return ORG_STAFF_CNT;
    }

    /**
     * @return ORG_STUS_CDE
     */
    public short getORG_STUS_CDE() {
        return ORG_STUS_CDE;
    }

    /**
     * @return ORG_TOTL_MBSHP_CNT
     */
    public int getORG_TOTL_MBSHP_CNT() {
        return ORG_TOTL_MBSHP_CNT;
    }

    /**
     * @return PRVCL_SERV_TAX_EXPTN_IND
     */
    public char getPRVCL_SERV_TAX_EXPTN_IND() {
        return PRVCL_SERV_TAX_EXPTN_IND;
    }

    /**
     * @return PST_EXPTN_IND
     */
    public char getPST_EXPTN_IND() {
        return PST_EXPTN_IND;
    }

    /**
     * @return TOP_LIB_ORG_NBR
     */
    public int getTOP_LIB_ORG_NBR() {
        return TOP_LIB_ORG_NBR;
    }

    /**
     * @return TRLTN_AGNCY_SMBL_CDE
     */
    public String getTRLTN_AGNCY_SMBL_CDE() {
        return TRLTN_AGNCY_SMBL_CDE;
    }

    /**
     * @param s
     */
    public void setBLNG_GRP_1_CDE(short s) {
        BLNG_GRP_1_CDE = s;
    }

    /**
     * @param s
     */
    public void setBLNG_GRP_SEQ_NBR(short s) {
        BLNG_GRP_SEQ_NBR = s;
    }

    /**
     * @param c
     */
    public void setFNC_ACNT_STNDG_IND(char c) {
        FNC_ACNT_STNDG_IND = c;
    }

    /**
     * @param string
     */
    public void setFNC_ACNT_STNDG_NTE(String string) {
        FNC_ACNT_STNDG_NTE = string;
    }

    /**
     * @param i
     */
    public void setFNC_CSTMR_NBR(int i) {
        FNC_CSTMR_NBR = i;
    }

    /**
     * @param f
     */
    public void setFNC_DPST_ACNT_AMT(float f) {
        FNC_DPST_ACNT_AMT = f;
    }

    /**
     * @param s
     */
    public void setGOVT_DEPT_CDE(short s) {
        GOVT_DEPT_CDE = s;
    }

    /**
     * @param s
     */
    public void setGOVT_TYP_CDE(short s) {
        GOVT_TYP_CDE = s;
    }

    /**
     * @param c
     */
    public void setGST_EXPTN_IND(char c) {
        GST_EXPTN_IND = c;
    }

    /**
     * @param s
     */
    public void setLANG_PRFNC_CDE(short s) {
        LANG_PRFNC_CDE = s;
    }

    /**
     * @param s
     */
    public void setLCKG_VRSN_UPDT_NBR(short s) {
        LCKG_VRSN_UPDT_NBR = s;
    }

    /**
     * @param i
     */
    public void setNLC_RESP_CNTR_NBR(int i) {
        NLC_RESP_CNTR_NBR = i;
    }

    /**
     * @param s
     */
    public void setORG_BSNS_TYP_CDE(short s) {
        ORG_BSNS_TYP_CDE = s;
    }

    /**
     * @param c
     */
    public void setORG_CDN_IND(char c) {
        ORG_CDN_IND = c;
    }

    /**
     * @param date
     */
    public void setORG_CRTN_DTE(Date date) {
        ORG_CRTN_DTE = date;
    }

    /**
     * @param string
     */
    public void setORG_ENG_MSSN_DSC(String string) {
        ORG_ENG_MSSN_DSC = string;
    }

    /**
     * @param string
     */
    public void setORG_FR_MSSN_DSC(String string) {
        ORG_FR_MSSN_DSC = string;
    }

    /**
     * @param c
     */
    public void setORG_LIB_IND(char c) {
        ORG_LIB_IND = c;
    }

    /**
     * @param date
     */
    public void setORG_LST_UPDT_TMEST(Date date) {
        ORG_LST_UPDT_TMEST = date;
    }

    /**
     * @param i
     */
    public void setORG_NBR(int i) {
        ORG_NBR = i;
    }

    /**
     * @param string
     */
    public void setORG_NTE(String string) {
        ORG_NTE = string;
    }

    /**
     * @param string
     */
    public void setORG_PFMNC_NTE(String string) {
        ORG_PFMNC_NTE = string;
    }

    /**
     * @param c
     */
    public void setORG_PRFT_NON_PRFT_IND(char c) {
        ORG_PRFT_NON_PRFT_IND = c;
    }

    /**
     * @param i
     */
    public void setORG_STAFF_CNT(int i) {
        ORG_STAFF_CNT = i;
    }

    /**
     * @param s
     */
    public void setORG_STUS_CDE(short s) {
        ORG_STUS_CDE = s;
    }

    /**
     * @param i
     */
    public void setORG_TOTL_MBSHP_CNT(int i) {
        ORG_TOTL_MBSHP_CNT = i;
    }

    /**
     * @param c
     */
    public void setPRVCL_SERV_TAX_EXPTN_IND(char c) {
        PRVCL_SERV_TAX_EXPTN_IND = c;
    }

    /**
     * @param c
     */
    public void setPST_EXPTN_IND(char c) {
        PST_EXPTN_IND = c;
    }

    /**
     * @param i
     */
    public void setTOP_LIB_ORG_NBR(int i) {
        TOP_LIB_ORG_NBR = i;
    }

    /**
     * @param string
     */
    public void setTRLTN_AGNCY_SMBL_CDE(String string) {
        TRLTN_AGNCY_SMBL_CDE = string;
    }

}
