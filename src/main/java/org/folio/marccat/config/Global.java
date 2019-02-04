package org.folio.marccat.config;

import net.sf.hibernate.cfg.Configuration;
import org.folio.marccat.dao.*;
import org.folio.marccat.dao.persistence.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map;

/**
 * Global constants.
 * With "Global" we mean a constant that
 *
 * <ul>
 * <li>is supposed to be shared at least between 2 modules.</li>
 * <li>needs to be used within this "shared" module</li>
 * </ul>
 *
 * @author cchiama
 * @author natasciab
 * @since 1.0
 */
public abstract class Global {

  public static final String MODULE_NAME = "MARCCAT";
  public static final String OKAPI_TENANT_HEADER_NAME = "X-Okapi-Tenant";
  public static final String OKAPI_TOKEN_HEADER_NAME = "X-Okapi-Token";
  public static final String EMPTY_STRING = "";
  public static final String SUBFIELD_DELIMITER_FOR_VIEW = "\\$";
  public static final String MAP_CODE = "a";
  public static final String ELECTRONIC_RESOURCE = "c";
  public static final String GLOBE = "d";
  public static final String TACTILE_MATERIAL = "f";
  public static final String PROJECTED_GRAPHIC = "g";
  public static final String MICROFORM = "h";
  public static final String NON_PROJECTED_GRAPHIC = "k";
  public static final String MOTION_PICTURE = "m";
  public static final String KIT_CODE = "o";
  public static final String NOTATED_MUSIC = "q";
  public static final String REMOTE_SENSING_IMAGE = "r";
  public static final String SOUND_RECORDING = "s";
  public static final String TEXT_CODE = "t";
  public static final String VIDEO_RECORDING = "v";
  public static final String UNSPECIFIED = "z";
  public static final List<String> FIXED_FIELDS = Arrays.asList("000", "001", "005", "006", "007", "008");
  public static final List<String> MANDATORY_FIELDS = Arrays.asList("000", "001", "008", "040");
  public static final String HEADER_TYPE_LABEL = "HEADER_TYPE";
  public static final String FORM_OF_MATERIAL_LABEL = "FORM_OF_MATERIAL";
  public static final String MATERIAL_TYPE_CODE_LABEL = "MATERIAL_TYPE_CODE";
  public static final String LEADER_TAG_NUMBER = "000";
  public static final String CONTROL_NUMBER_TAG_CODE = "001";
  public static final String CATALOGING_SOURCE_TAG_CODE = "040";
  public static final String DATETIME_TRANSACTION_TAG_CODE = "005";
  public static final String OTHER_MATERIAL_TAG_CODE = "006";
  public static final String PHYSICAL_DESCRIPTION_TAG_CODE = "007";
  public static final int MATERIAL_FIELD_LENGTH = 40;
  public static final int OTHER_MATERIAL_FIELD_LENGTH = 18;
  public static final String ITEM_DATE_FIRST_PUBLICATION = "    ";
  public static final String ITEM_DATE_LAST_PUBLICATION = "    ";
  public static final String MATERIAL_TAG_CODE = "008";
  public static final int INT_CATEGORY = 1;
  public static final int PHYSICAL_UNSPECIFIED_HEADER_TYPE = 45;
  public static final int LEADER_LENGTH = 24;
  public static final DecimalFormat DECIMAL_FORMAT_AN = new DecimalFormat("000000000000");
  public static final String BOOKFORM_OF_MATERIAL = "bk";
  public static final String LOADING_FILE_FILENAME = "filename";
  public static final String LOADING_FILE_IDS = "ids";
  public static final String LOADING_FILE_REJECTED = "rejected";
  public static final String LOADING_FILE_ADDED = "added";
  public static final String LOADING_FILE_ERRORS = "errors";
  public static final String AN_KEY_CODE_FIELD = "BI";
  public static final String ERROR_MANDATORY_TAG = "-1";
  public static final String ERROR_DUPLICATE_TAG = "-2";
  public static final String ERROR_EMPTY_TAG = "-3";
  public static final String NO_RECORD_FOUND = "-4";
  public static final Map<String, String> ERRORS_MAP = new HashMap<String, String>() {
    {
      put(ERROR_MANDATORY_TAG, "Check mandatory tags failure.");
      put(ERROR_DUPLICATE_TAG, "Duplicate tags for : %s");
      put(ERROR_EMPTY_TAG, "Some tags appears empties: %s.");
      put(NO_RECORD_FOUND, "Record not found: %d.");
    }
  };
  public static final int TAG_RELATION_MIN = 760;
  public static final int TAG_RELATION_MAX = 787;
  public static final int DATETIME_TRANSACTION_HEADER_TYPE = 41;
  public static final short CORRELATION_UNDEFINED = -1;
  public static final int CATALOGING_SOURCE_HEADER_TYPE = 1;
  public static final int LEADER_HEADER_TYPE = 15;
  public static final int CONTROL_NUMBER_HEADER_TYPE = 39;
  public static final int DATETIME_TRANSACION_HEADER_TYPE = 41;
  public static final int MATERIAL_DESCRIPTION_HEADER_TYPE = 31;
  public static final String FIXED_LEADER_LENGTH = "00000";
  public static final char RECORD_STATUS_CODE = 'n';
  public static final char RECORD_TYPE_CODE = 'a';
  public static final char BIBLIOGRAPHIC_LEVEL_CODE = 'm';
  public static final char CONTROL_TYPE_CODE = ' ';
  public static final char CHARACTER_CODING_SCHEME_CODE = ' ';
  public static final String FIXED_LEADER_BASE_ADDRESS = "2200000";
  public static final char ENCODING_LEVEL = ' ';
  public static final char DESCRIPTIVE_CATALOGUING_CODE = ' ';
  public static final char LINKED_RECORD_CODE = ' ';
  public static final String FIXED_LEADER_PORTION = "4500";
  public static final Map<Integer, String> PHYSICAL_TYPES_MAP = new HashMap<Integer, String>() {
    {
      put(23, GLOBE);
      put(24, MAP_CODE);
      put(25, MICROFORM);
      put(26, MOTION_PICTURE);
      put(27, NON_PROJECTED_GRAPHIC);
      put(28, PROJECTED_GRAPHIC);
      put(29, SOUND_RECORDING);
      put(30, VIDEO_RECORDING);
      put(42, ELECTRONIC_RESOURCE);
      put(43, REMOTE_SENSING_IMAGE);
      put(44, TEXT_CODE);
      put(45, UNSPECIFIED);
      put(46, TACTILE_MATERIAL);
      put(47, KIT_CODE);
      put(48, NOTATED_MUSIC);
    }
  };
  public static final int HEADER_CATEGORY = 1;
  public static final int NAME_CATEGORY = 2;
  public static final int TITLE_CATEGORY = 3;
  public static final int SUBJECT_CATEGORY = 4;
  public static final int CONTROL_NUMBER_CATEGORY = 5;
  public static final int CLASSIFICATION_CATEGORY = 6;
  public static final int PUBLISHER_CATEGORY = 7;
  public static final int BIB_NOTE_CATEGORY = 7;
  public static final int NAME_TITLE_CATEGORY = 11;
  public static final int DEFAULT_AVAILABILITY_STATUS = 99;
  public static final String DEFAULT_LEVEL_CARD = "L01";
  public static final String DEFAULT_MOTHER_LEVEL = "001";
  public static final String DEFAULT_LEVEL_NATURE = "001";
  public static final String YES_FLAG = "S";
  public static final String NO_FLAG = "N";
  public static final String TITLE_REQUIRED_PERMISSION = "editTitle";
  public static final String NAME_REQUIRED_PERMISSION = "editName";
  public static final String CNTL_NBR_REQUIRED_PERMISSION = "editControlNumber";
  public static final String PUBLISHER_REQUIRED_PERMISSION = "editNotes";
  public static final String CLASSIFICATION_REQUIRED_PERMISSION = "editClassNumber";
  public static final String SUBJECT_REQUIRED_PERMISSION = "editSubject";
  public static final String NOTE_REQUIRED_PERMISSION = "editNote";
  public static final String TITLE_VARIANT_CODES = "3civ5";
  public static final String TITLE_ISSN_SERIES_SUBFIELD_CODE = "x";
  public static final String TITLE_VOLUME_SUBFIELD_CODE = "v";
  public static final String NAME_VARIANT_SUBFIELD_CODES = "3eiuox45";
  public static final String NAME_TITLE_INSTITUTION_SUBFIELD_CODE = "5";
  public static final String WORK_REL_SUBFIELD_CODE = "4";
  public static final int PUBLISHER_DEFAULT_NOTE_TYPE = 24;
  public static final String PUBLISHER_FAST_PRINTER_SUBFIELD_CODES = "368efg";
  public static final String PUBLISHER_VARIANT_CODES = "368cefg";
  public static final String PUBLISHER_OTHER_SUBFIELD_CODES = "cefg";
  public static final int DEWEY_TYPE_CODE = 12;
  public static final String SUBJECT_VARIANT_CODES = "34eu";
  public static final String SUBJECT_WORK_REL_STRING_TEXT_SUBFIELD_CODES = "eu";
  public static final int STANDARD_NOTE_MAX_LENGHT = 1024;
  public static final int OVERFLOW_NOTE_MAX_LENGHT = 1000;
  public static final String NAME_TITLE_VARIANT_CODES = "3v5";
  public static final Map<String, Class> MAP_CODE_LISTS = new HashMap<String, Class>() {
    {
      put("BOOK_MATERIAL_CODE", T_BOOK_TYP_CDE.class);
      put("MUSIC_MATERIAL_CODE", T_MSC_TYP_CDE.class);
      put("MAP_MATERIAL_CODE", T_CRTGC_TYP_CDE.class);
      put("VM_MATERIAL_CODE", T_VSL_TYP_CDE.class);
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
      put("UNS_SPEC_DESIGN", T_USP_SMD_CDE.class); //TODO FIXME wrong
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
  public static final Map<String, Class> BIBLIOGRAPHIC_ACCESS_POINT_CLASS_MAP = new HashMap<String, Class>() {
    {
      put("NH", NameAccessPoint.class);
      put("TH", TitleAccessPoint.class);
      put("SH", SubjectAccessPoint.class);
      put("MH", NameTitleAccessPoint.class);
    }
  };

  public static final Map<String, String> LANGUAGES = new HashMap<String, String>() {
    {
      put("ITA", "ita");
      put("EN", "en");
    }
  };

    public static final Map<String, String> INDEX_AUTHORITY_TYPE_MAP = new HashMap<String, String>() {
    {
      put("NH", "NK");
      put("TH", "TK");
      put("SH", "SK");
      put("MH", "NTK");
    }
  };
  public static final Map<String, Class> DAO_CLASS_MAP = new HashMap<String, Class>() {
    {
      put("2P0", NameDescriptorDAO.class);
      put("3P10", NameDescriptorDAO.class);
      put("4P10", NameDescriptorDAO.class);
      put("5P10", NameDescriptorDAO.class);
      put("7P0", TitleDescriptorDAO.class);
      put("9P0", SubjectDescriptorDAO.class);
      put("230P", PublisherNameDescriptorDAO.class);
      put("243P", PublisherPlaceDescriptorDAO.class);
      put("250S", NameTitleNameDescriptorDAO.class);
      put("251S", NameTitleTitleDescriptorDAO.class);
      put("16P30", ControlNumberDescriptorDAO.class);
      put("18P2", ControlNumberDescriptorDAO.class);
      put("19P2", ControlNumberDescriptorDAO.class);
      put("20P3", ControlNumberDescriptorDAO.class);
      put("21P2", ControlNumberDescriptorDAO.class);
      put("22P10", ControlNumberDescriptorDAO.class);
      put("29P20", ControlNumberDescriptorDAO.class);
      put("30P4", ControlNumberDescriptorDAO.class);
      put("31P3", ControlNumberDescriptorDAO.class);
      put("32P3", ControlNumberDescriptorDAO.class);
      put("33P3", ControlNumberDescriptorDAO.class);
      put("34P20", ControlNumberDescriptorDAO.class);
      put("35P20", ControlNumberDescriptorDAO.class);
      put("36P20", ControlNumberDescriptorDAO.class);
      put("51P3", ControlNumberDescriptorDAO.class);
      put("52P3", ControlNumberDescriptorDAO.class);
      put("53P3", ControlNumberDescriptorDAO.class);
      put("54P3", ControlNumberDescriptorDAO.class);
      put("55P3", ControlNumberDescriptorDAO.class);
      put("47P40", ClassificationDescriptorDAO.class);
      put("24P5", ClassificationDescriptorDAO.class);
      put("25P5", ClassificationDescriptorDAO.class);
      put("27P5", ClassificationDescriptorDAO.class);
      put("23P5", ClassificationDescriptorDAO.class);
      put("48P3", ClassificationDescriptorDAO.class);
      put("46P40", ClassificationDescriptorDAO.class);
      put("50P3", ClassificationDescriptorDAO.class);
      put("49P3", ClassificationDescriptorDAO.class);
      put("326P1", ClassificationDescriptorDAO.class);
      put("353P1", ClassificationDescriptorDAO.class);
      put("303P3", ClassificationDescriptorDAO.class);
      put("28P30", ShelfListDAO.class);
      put("244P30", ShelfListDAO.class);
      put("47P30", ShelfListDAO.class);
      put("37P30", ShelfListDAO.class);
      put("38P30", ShelfListDAO.class);
      put("39P30", ShelfListDAO.class);
      put("41P30", ShelfListDAO.class);
      put("42P30", ShelfListDAO.class);
      put("43P30", ShelfListDAO.class);
      put("44P30", ShelfListDAO.class);
      put("45P30", ShelfListDAO.class);
      put("46P30", ShelfListDAO.class);
      put("373P0", SubjectDescriptorDAO.class);
    }
  };
  public static final Map<String, String> FILTER_MAP = new HashMap<String, String>() {
    {
      put("2P0", "");
      put("3P10", " and hdg.typeCode = 2 ");
      put("4P10", " and hdg.typeCode = 3 ");
      put("5P10", " and hdg.typeCode = 4 ");
      put("7P0", "");
      put("9P0", "");
      put("230P", "");
      put("243P", "");
      put("250S", "");
      put("251S", "");
      put("16P30", "");
      put("18P2", " and hdg.typeCode = 9 ");
      put("19P2", " and hdg.typeCode = 10 ");
      put("20P3", " and hdg.typeCode = 93 ");
      put("21P2", " and hdg.typeCode = 2 ");
      put("22P10", " and hdg.typeCode = 93 ");
      put("29P20", " and hdg.typeCode = 71 ");
      put("30P4", "");
      put("31P3", " and hdg.typeCode = 84 ");
      put("32P3", " and hdg.typeCode = 88 ");
      put("33P3", " and hdg.typeCode = 90 ");
      put("34P20", "");
      put("35P20", "");
      put("36P20", " and hdg.typeCode = 52 ");
      put("51P3", " and hdg.typeCode = 89 ");
      put("52P3", " and hdg.typeCode = 83 ");
      put("53P3", " and hdg.typeCode = 91 ");
      put("54P3", " and hdg.typeCode = 97 ");
      put("55P3", " and hdg.typeCode = 98 ");
      put("47P40", " and hdg.typeCode = 21");
      put("24P5", " and hdg.typeCode = 12");
      put("25P5", " and hdg.typeCode = 1");
      put("27P5", " and hdg.typeCode = 6");
      put("23P5", " and hdg.typeCode not in (1,6,10,11,12,14,15,29) ");
      put("48P3", " and hdg.typeCode = 10");
      put("46P40", " and hdg.typeCode = 11");
      put("50P3", " and hdg.typeCode = 14");
      put("49P3", " and hdg.typeCode = 15");
      put("326P1", " and hdg.typeCode = 29");
      put("28P30", " and hdg.typeCode = '@'");
      put("244P30", " and hdg.typeCode = 'N'");
      put("47P30", " and hdg.typeCode = 'M'");
      put("37P30", " and hdg.typeCode = '2'");
      put("38P30", " and hdg.typeCode = '3'");
      put("39P30", " and hdg.typeCode = '4'");
      put("41P30", " and hdg.typeCode = '6'");
      put("42P30", " and hdg.typeCode = 'A'");
      put("43P30", " and hdg.typeCode = 'C'");
      put("44P30", " and hdg.typeCode = 'E'");
      put("45P30", " and hdg.typeCode = 'F'");
      put("46P30", " and hdg.typeCode = 'G'");
      put("303P3", " and hdg.typeCode = 13");
      put("354P0", "");
      put("353P1", " and hdg.typeCode = 80");
      put("373P0", " and hdg.sourceCode = 4 ");
    }
  };
  public static String SUBFIELD_DELIMITER = "\u001f";
  public static Configuration HCONFIGURATION = new Configuration();
  public static String SCHEMA_SUITE_KEY = "SUITE_KEY";

  static {
    HCONFIGURATION.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
    HCONFIGURATION.setProperty("dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
    HCONFIGURATION.setProperty("show_sql", System.getProperty("show.sql", "false"));
    try {
      HCONFIGURATION.configure("/hibernate.cfg.xml");
    } catch (final Throwable failure) {
      throw new ExceptionInInitializerError(failure);
    }
  }

  public static final String SPECIFIC_MATERIAL_DESIGNAION_ON_CODE = "specificMaterialDesignationCode";
  public static final String COLOR_CODE  = "colorCode";
  public static final String SOUND_ON_MEDIUM_OR_SEPARATE_CODE = "soundOnMediumOrSeparateCode";
  public static final String MEDIUM_FOR_SOUND_CODE = "mediumForSoundCode";
  public static final String DIMENSION_CODE = "dimensionCodes";
  public static final String CONFIGURATION_CODE = "configurationCode";
  public static final String POLARITY_CODE = "polarityCode";
  public static final String GOVERNMENT_PUBLICATION_CODE = "governmentPublicationCode";
  public static final String FORM_OF_ITEM_CODE = "formOfItemCode";
  public static final String BOOK_TYPE = "bk";
  public static final String MUSIC_TYPE = "msr";
  public static final String SERIAL_TYPE = "se";
  public static final String MIXED_TYPE = "mm";
  public static final String MAP_TYPE = "cm";
  public static final String VISUAL_TYPE = "vm";
  public static final String COMPUTER_TYPE = "cf";

  public static final int CONTROL_FIELD_CATEGORY_CODE = 1;

  public static final char BIBLIOGRAPHIC_INDICATOR_NOT_NUMERIC = 'S';

  public static final List<String> NAMES = Arrays.asList("100", "110", "111"	);

  public static final List<String> NAMES_D = Arrays.asList("110", "111", "710", "711");

  public static final List<String> NAMES_E = Arrays.asList("100", "110", "120", "121", "240", "243", "400", "410", "600", "610", "700", "710", "720", "721", "800", "810", "900", "910", "980", "981");

  public static final List<String> NAMES_X = Arrays.asList("400", "410", "411", "700", "710", "711", "720", "721", "722", "900", "910", "911", "980", "981", "982");

  public static final List<String> NAMES_V = Arrays.asList("400", "410", "411", "800", "810", "811", "980", "981", "982");

  public static final List<String> NAMES_245 = Arrays.asList("600", "610", "611", "700", "710", "711", "720", "721", "722", "800", "810", "811", "900", "910", "911", "980", "981", "982");

  public static final List<String> SUBJECTS_4 =  Arrays.asList("600", "610", "611");

  public static final List<String> SUBJECTS_E =  Arrays.asList("600", "610");

  public static final List<String> TITLES_X = Arrays.asList("440", "730", "740", "930", "983");

  public static final List<String> TITLES_V = Arrays.asList("440", "830", "983");

  public static final List<String> TITLES = Arrays.asList("130", "241", "245", "730", "740", "830", "930", "941", "945", "983");

  public static final String TERMINAL_PUNCTUATION = ".?!)-";

  public static final String OTHER_TERMINAL_PUNCTUATION = ".?!)]-";
}
