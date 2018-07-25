package org.folio.cataloging.integration;

import org.folio.cataloging.dao.persistence.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Global constants of storage service.
 *
 * @author natasciab
 * @since 1.0
 */
public class GlobalStorage {
    public final static String HEADER_TYPE_LABEL = "HEADER_TYPE";
    public final static String FORM_OF_MATERIAL_LABEL = "FORM_OF_MATERIAL";
    public final static String MATERIAL_TYPE_CODE_LABEL = "MATERIAL_TYPE_CODE";
    public final static String MATERIAL_TAG_CODE = "008";
    public final static String DOLLAR = "\u001f";

    public final static int NAME_CATEGORY = 2;
    public final static int TITLE_CATEGORY = 3;
    public final static int SUBJECT_CATEGORY = 4;
    public final static int CONTROL_NUMBER_CATEGORY = 5;
    public final static int CLASSIFICATION_CATEGORY = 6;
    public final static int PUBLISHER_CATEGORY = 7;
    public final static int BIB_NOTE_CATEGORY = 7;

    public final static int DEFAULT_AVAILABILITY_STATUS = 99;
    public final static String DEFAULT_LEVEL_CARD = "L01";
    public final static String DEFAULT_MOTHER_LEVEL = "001";
    public final static String DEFAULT_LEVEL_NATURE = "001";
    public final static String YES_FLAG = "S";
    public final static String NO_FLAG = "N";
    public final static String CHARSET_UTF8 = "UTF-8";

    public final static String TITLE_REQUIRED_PERMISSION = "editTitle";
    public final static String NAME_REQUIRED_PERMISSION = "editName";
    public final static String CNTL_NBR_REQUIRED_PERMISSION = "editControlNumber";
    public final static String PUBLISHER_REQUIRED_PERMISSION = "editNotes";
    public final static String CLASSIFICATION_REQUIRED_PERMISSION = "editClassNumber";
    public final static String SUBJECT_REQUIRED_PERMISSION = "editSubject";
    public final static String NOTE_REQUIRED_PERMISSION = "editNote";

    public final static String TITLE_VARIANT_CODES = "3civ5";
    public final static String TITLE_ISSN_SERIES_SUBFIELD_CODE = "x";
    public final static String TITLE_VOLUME_SUBFIELD_CODE = "v";

    public final static String NAME_VARIANT_SUBFIELD_CODES = "3eiuox45";
    public final static String NAME_WORK_REL_STRING_TEXT_SUBFIELD_CODES = "eju";
    public final static String NAME_OTHER_SUBFIELD_CODES = "iox";
    public final static String NAME_TITLE_INSTITUTION_SUBFIELD_CODE = "5";

    public final static String WORK_REL_SUBFIELD_CODE = "4";

    public final static int PUBLISHER_DEFAULT_NOTE_TYPE = 24;

    public final static String PUBLISHER_FAST_PRINTER_SUBFIELD_CODES = "368efg";
    public final static String PUBLISHER_VARIANT_CODES = "368cefg";
    public final static String PUBLISHER_OTHER_SUBFIELD_CODES = "cefg";

    public final static int DEWEY_TYPE_CODE = 12;
    public final static String SUBJECT_VARIANT_CODES = "34eu";
    public final static String SUBJECT_WORK_REL_STRING_TEXT_SUBFIELD_CODES = "eu";

    public final static int STANDARD_NOTE_MAX_LENGHT = 1024;
    public final static int OVERFLOW_NOTE_MAX_LENGHT = 1000;

    public final static String NAME_TITLE_VARIANT_CODES = "3v5";


    public final static Map<String, Class> MAP_CODE_LISTS = new HashMap<String, Class>(){
        {
            //008-006
            put("DATE_TYPE", T_ITM_DTE_TYP.class);
            put("MODIFIED_RECORD_TYPE", T_REC_MDFTN.class);
            put("CATALOGUING_SOURCE", T_REC_CTLGG_SRC.class);
            put("BOOK_ILLUSTRATION", T_BOOK_ILSTN.class);
            put("TARGET_AUDIENCE", T_TRGT_AUDNC.class);
            put("FORM_OF_ITEM", T_FORM_OF_ITM.class);
            put("NATURE_OF_CONTENT", T_NTR_OF_CNTNT.class);
            put("GOV_PUBLICATION", T_GOVT_PBLTN.class);
            put("CONF_PUBLICATION", T_CONF_PBLTN.class);
            put("BOOK_FESTSCHRIFT", T_BOOK_FTSCT.class);
            put("BOOK_INDEX", T_BOOK_IDX_AVBTY.class);
            put("BOOK_LITERARY_FORM", T_BOOK_LTRY_FORM_TYP.class);
            put("BOOK_BIOGRAPHY", T_BOOK_BGPHY.class);
            put("MSC_FORM_OF_COMPOSITION", T_MSC_FORM_OR_TYP.class);
            put("MSC_FORMAT", T_MSC_FRMT.class);
            put("MSC_PARTS", T_MSC_PRT.class);
            put("MSC_TEXTUAL_MAT_CODE", T_MSC_TXTL_MTR.class);
            put("MSC_LITERARY_TEXT", T_MSC_LTRY_TXT.class);
            put("MSC_TRANSPOSITION_CODE", T_MSC_TRNSPSN_ARRNGMNT.class);
            put("SRL_FREQUENCY", T_SRL_FREQ.class);
            put("SRL_REGULARITY", T_SRL_REGTY.class);
            put("SRL_TYPE_CONTINUING_RESOURCE", T_SRL_TYP.class);
            put("SRL_FORM_ORGNL_ITEM", T_SRL_FORM_ORGNL_ITM.class);
            put("SRL_NATURE_OF_WORK", T_NTR_OF_CNTNT.class);
            put("SRL_ORIGIN_ALPHABET", T_SRL_TTL_ALPBT.class);
            put("SRL_ENTRY_CONVENTION", T_SRL_SCSV_LTST.class);
            put("MAP_RELIEF", T_CRTGC_RLF.class);
            put("MAP_PROJECTION", T_CRTGC_PRJTN.class);
            put("MAP_TYPE_MATERIAL", T_CRTGC_MTRL.class);
            put("MAP_INDEX", T_CRTGC_IDX_AVBTY.class);
            put("MAP_SPECIAL_FORMAT_CHARACTERISTIC", T_CRTGC_FRMT.class);
            put("VSL_TARGET_AUDIENCE", T_VSL_TRGT_AUDNC.class);
            put("VSL_TYPE_MATERIAL", T_VSL_MTRL_TYP.class);
            put("VSL_TECHNIQUE", T_VSL_TECH.class);
            put("COMPUTER_TARGET_AUDIENCE", T_CMPTR_TRGT_AUDNC.class);
            put("COMPUTER_FORM_OF_ITEM", T_CF_FORM_OF_ITM.class);
            put("COMPUTER_TYPE_MATERIAL", T_CMPTR_FIL_TYP.class);
            //007
            put("CATEGORY_MATERIAL", GeneralMaterialDesignation.class);
            put("SOUND_MEDIUM_OR_SEP", T_SND_MDM_OR_SEPRT.class);
            put("MEDIUM_FOR_SOUND", T_MDM_FOR_SND.class);
            put("MAP_SPEC_DESIGN", T_MAP_SMD.class);
            put("MAP_COLOR", T_MAP_CLR.class);
            put("MAP_PHYSICAL_MEDIUM", T_MAP_PHSCL_MDM.class);
            put("MAP_TYPE_OF_REPRODUCTION", T_MAP_RPRDT_TYP.class);
            put("MAP_PRODUCTION_DETAILS", T_MAP_PRDTN_DTL.class);
            put("MAP_POLARITY", T_MAP_PLRTY.class);
            put("CF_SPEC_DESIGN", T_CF_SMD.class);
            put("CF_COLOR", T_CF_CLR.class);
            put("CF_DIMENSIONS", T_CF_DMNSN.class);
            put("CF_FILE_FORMAT", T_CF_FF.class);
            put("CF_QUALITY_ASS", T_CF_QAT.class);
            put("CF_ANTECEDENT_SRC", T_CF_ANTSRC.class);
            put("CF_COMPRESSION_LVL", T_CF_LOC.class);
            put("CF_REFORMATTING_QUALITY", T_CF_RQ.class);
            put("GLB_SPEC_DESIGN", T_GLB_SMD.class);
            put("GLB_COLOR", T_GLB_CLR.class);
            put("GLB_PHYSICAL_MEDIUM", T_GLB_PHSCL_MDM.class);
            put("GLB_TYPE_OF_REPRODUCTION", T_GLB_RPDTN_TYP.class);
            put("TCT_SPEC_DESIGN", T_TM_SMD.class);
            put("TCT_CLASS_BRAILLE_WRITING", T_TM_CBW.class);
            put("TCT_CONTRACTION_LVL", T_TM_LC.class);
            put("TCT_BRAILLE_MUSIC_FORMAT", T_TM_BMF.class);
            put("TCT_SPECIAL_PHYSICAL_CHAR", T_TM_SPC.class);
            put("PG_SPEC_DESIGN", T_PG_SMD.class);
            put("PG_COLOR", T_PG_CLR.class);
            put("PG_EMUL_BASE", T_PG_BSE_OF_EMLSN_MTRL.class);
            put("PG_DIMENSIONS", T_PG_DMNSN.class);
            put("PG_SECONDARY_SUPPORT", T_PG_SCDRY_SPRT_MTRL.class);
            put("NPG_SPEC_DESIGN", T_NPG_SMD.class);
            put("NPG_COLOR", T_NPG_CLR.class);
            put("NPG_PRIMARY_SUPPORT", T_NPG_PRMRY_SPRT_MTRL.class);
            put("NPG_SECONDARY_SUPPORT", T_NPG_SCDRY_SPRT_MTRL.class);
            put("MP_SPEC_DESIGN", T_MP_SMD.class);
            put("MP_COLOR", T_MP_CLR.class);
            put("MP_PRESENT_FORMAT", T_MP_PRSTN_FRMT.class);
            put("MP_DIMENSIONS", T_MP_DMNSN.class);
            put("MP_CONF_PLAYBACK", T_MP_CONFIG.class);
            put("MP_PROD_ELEM", T_MP_PROD_ELEM.class);
            put("MP_POLARITY", T_MP_POS_NEG.class);
            put("MP_GENERATION", T_MP_GNRTN.class);
            put("MP_BASE_FILM", T_MP_BSE_FLM.class);
            put("MP_REFINE_CAT_COLOR", T_MP_RF_CLR.class);
            put("MP_KIND_COLORS", T_MP_CLR_STCK.class);
            put("MP_DETERIORATION_STAGE", T_MP_DTRTN_STGE.class);
            put("MP_COMPLETENESS", T_MP_CMPLT.class);
            put("KIT_SPEC_DESIGN", T_KIT_SMD.class);
            put("NMU_SPEC_DESIGN", T_NM_SMD.class);
            put("TXT_SPEC_DESIGN", T_TXT_SMD.class);
            put("UNS_SPEC_DESIGN", T_USP_SMD_CDE.class); //errata
            put("RSI_SPEC_DESIGN", T_RSI_SMD.class);
            put("RSI_ALTITUDE", T_RSI_ALT_SENS.class);
            put("RSI_ATTITUDE", T_RSI_ATT_SENS.class);
            put("RSI_CLOUD_COVER", T_RSI_CLD_CVR.class);
            put("RSI_PLAT_CONSTRUCTION", T_RSI_PLTFRM_CNSTRCT.class);
            put("RSI_PLAT_USE", T_RSI_PLTFRM_USE.class);
            put("RSI_SENSOR_TYPE", T_RSI_SNSR_TPE.class);
            put("RSI_DATA_TYPE", T_RSI_DATA_TPE.class);
            put("SND_SPEC_DESIGN", T_SND_SMD.class);
            put("SND_SPEED", T_SND_SPD.class);
            put("SND_CONF_PLAYBACK", T_SND_PLYBC_CHNL_CFGTN.class);
            put("SND_GROOVE_WIDTH", T_SND_DISC_GRV_WDTH.class);
            put("SND_DIMENSIONS", T_SND_DMNSN.class);
            put("SND_TAPE_WIDTH", T_SND_TAPE_WDTH.class);
            put("SND_TAPE_CONF", T_SND_TAPE_CFGTN.class);
            put("SND_DISC_TYPE", T_SND_DISC_CYLND_TYP.class);
            put("SND_MATERIAL_TYPE", T_SND_MTRL_TYP.class);
            put("SND_CUTTING", T_SND_DISC_CTG.class);
            put("SND_SPEC_PLAYBACK", T_SND_SPCL_PLYBC_CHAR.class);
            put("SND_STORAGE_TECNIQUE", T_SND_STRG_TECH.class);
            put("VR_SPEC_DESIGN", T_VR_SMD.class);
            put("VR_COLOR", T_VR_CLR.class);
            put("VR_FORMAT", T_VR_FRMT.class);
            put("VR_DIMENSIONS", T_VR_DMNSN.class);
            put("VR_CONF_PLAYBACK", T_VR_PLYBC_CHNL_CFGTN.class);
            put("MIC_COLOR", T_MIC_CLR.class);
            put("MIC_DIMENSIONS", T_MIC_DMNSN.class);
            put("MIC_BASE_FILM", T_MIC_FLM_BSE.class);
            put("MIC_EMUL_FILM", T_MIC_FLM_EMLSN.class);
            put("MIC_GENERATION", T_MIC_GNRTN.class);
            put("MIC_POLARITY", T_MIC_PLRTY.class);
            put("MIC_REDUCT_RATIO_RANGE", T_MIC_RDCTN_RATIO_RNG.class);
            put("MIC_SPEC_DESIGN", T_MIC_SMD.class);
        }
    };
}