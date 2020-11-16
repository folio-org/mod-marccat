package org.folio.marccat.config.constants;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.folio.marccat.dao.ClassificationDescriptorDAO;
import org.folio.marccat.dao.ControlNumberDescriptorDAO;
import org.folio.marccat.dao.NameDescriptorDAO;
import org.folio.marccat.dao.NameTitleNameDescriptorDAO;
import org.folio.marccat.dao.NameTitleTitleDescriptorDAO;
import org.folio.marccat.dao.PublisherNameDescriptorDAO;
import org.folio.marccat.dao.PublisherPlaceDescriptorDAO;
import org.folio.marccat.dao.ShelfListDAO;
import org.folio.marccat.dao.SubjectDescriptorDAO;
import org.folio.marccat.dao.TitleDescriptorDAO;
import org.folio.marccat.dao.persistence.*;

import net.sf.hibernate.cfg.Configuration;

/**
 * Global constants.
 *
 * @description: With "Global" we mean a constant that
 *
 *               <ul>
 *               <li>is supposed to be shared at least between 2 modules.</li>
 *               <li>needs to be used within this "shared" module</li>
 *               </ul>
 */
public class Global {

  public static final String BASE_URI = "marccat";
  public static final String MODULE_NAME = "MARCCAT";
  public static final String MODULE_MARCCAT = "mod-marccat";
  public static final String OKAPI_TENANT_HEADER_NAME = "X-Okapi-Tenant";
  public static final String OKAPI_URL = "X-Okapi-Url";
  public static final String OKAPI_TO_URL = "X-Okapi-Url-to";
  public static final String OKAPI_TOKEN_HEADER_NAME = "X-Okapi-Token";
  public static final String EMPTY_STRING = "";
  public static final String EMPTY_VALUE = " ";
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
  public static final String AN_KEY_AUT = "AA";
  public static final String ERROR_MANDATORY_TAG = "-1";
  public static final String ERROR_DUPLICATE_TAG = "-2";
  public static final String ERROR_EMPTY_TAG = "-3";
  public static final String NO_RECORD_FOUND = "-4";
  public static final String NO_TAG_FOUND = "-5";
  public static final Map<String, String> ERRORS_MAP;
  public static final int TAG_RELATION_MIN = 760;
  public static final int TAG_RELATION_MAX = 787;
  public static final int DATETIME_TRANSACTION_HEADER_TYPE = 41;
  public static final short CORRELATION_UNDEFINED = -1;
  public static final int CATALOGING_SOURCE_HEADER_TYPE = 1;
  public static final int LEADER_HEADER_TYPE = 15;
  public static final int CONTROL_NUMBER_HEADER_TYPE = 39;
  public static final int DATETIME_TRANSACION_HEADER_TYPE = 41;
  public static final int AUT_DATETIME_TRANSACION_HEADER_TYPE = 12;
  public static final int MATERIAL_DESCRIPTION_HEADER_TYPE = 31;
  public static final String FIXED_LEADER_LENGTH = "00000";
  public static final char RECORD_STATUS_CODE = 'n';
  public static final char RECORD_TYPE_CODE = 'a';
  public static final char AUT_RECORD_TYPE_CODE = 'z';
  public static final char BIBLIOGRAPHIC_LEVEL_CODE = 'm';
  public static final char CONTROL_TYPE_CODE = ' ';
  public static final char CHARACTER_CODING_SCHEME_CODE = ' ';
  public static final String FIXED_LEADER_BASE_ADDRESS = "2200000";
  public static final char ENCODING_LEVEL = ' ';
  public static final char AUT_ENCODING_LEVEL = 'n';
  public static final char DESCRIPTIVE_CATALOGUING_CODE = ' ';
  public static final char PUNCTUATION_POLICY = ' ';
  public static final char LINKED_RECORD_CODE = ' ';
  public static final String FIXED_LEADER_PORTION = "4500";
  public static final Map<Integer, String> PHYSICAL_TYPES_MAP;
  public static final int HEADER_CATEGORY = 1;
  public static final int NAME_CATEGORY = 2;
  public static final int TITLE_CATEGORY = 3;
  public static final int SUBJECT_CATEGORY = 4;
  public static final int CONTROL_NUMBER_CATEGORY = 5;
  public static final int CLASSIFICATION_CATEGORY = 6;
  public static final int PUBLISHER_CATEGORY = 7;
  public static final int BIB_NOTE_CATEGORY = 7;
  public static final int AUT_NOTE_CATEGORY = 7;
  public static final int NAME_TITLE_CATEGORY = 11;
  public static final int RELATION_CATEGORY = 8;
  public static final int AUT_NAME_CATEGORY = 17;
  public static final int AUT_TITLE_CATEGORY = 22;
  public static final int AUT_SUBJECT_CATEGORY = 18;
  public static final int AUT_NAME_TITLE_CATEOGRY = 11;
  public static final int AUTHORITY_MATERIAL_DESCRIPTION_HEADER_TYPE = 10;
  public static final String AUTHORITY_MATERIAL_DESCRIPTION_DESCRIPTION = "008";
  public static final String NAME_TYPE_HDG = "NH";
  public static final String TITLE_TYPE_HDG = "TH";
  public static final String SUBJECT_TYPE_HDG = "SH";
  public static final String NAME_TITLE_TYPE_HDG = "MH";
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
  public static final List<Integer> PUBLISHER_CODES = Arrays.asList(24, 381, 382, 410, 411, 412, 413, 414, 415, 416,
      417, 418, 419, 420, 421, 422, 423, 424);
  public static final int DEWEY_TYPE_CODE = 12;
  public static final String SUBJECT_VARIANT_CODES = "34eu";
  public static final String SUBJECT_WORK_REL_STRING_TEXT_SUBFIELD_CODES = "eu";
  public static final int STANDARD_NOTE_MAX_LENGHT = 1024;
  public static final int OVERFLOW_NOTE_MAX_LENGHT = 1000;
  public static final String NAME_TITLE_VARIANT_CODES = "3v5";
  public static final Map<String, Class<?>> MAP_CODE_LISTS;
  public static final Map<String, Class<?>> BIBLIOGRAPHIC_ACCESS_POINT_CLASS_MAP;
  public static final Map<String, String> INDEX_AUTHORITY_TYPE_MAP;
  public static final Map<String, Class<?>> DAO_CLASS_MAP;
  public static final Map<String, String> FILTER_MAP;
  public static final String SPECIFIC_MATERIAL_DESIGNAION_ON_CODE = "specificMaterialDesignationCode";
  public static final String COLOR_CODE = "colorCode";
  public static final String SOUND_ON_MEDIUM_OR_SEPARATE_CODE = "soundOnMediumOrSeparateCode";
  public static final String MEDIUM_FOR_SOUND_CODE = "mediumForSoundCode";
  public static final String DIMENSION_CODE = "dimensionCode";
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
  public static final List<String> FIXED_FIELDS = Arrays.asList("000", "001", "005", "006", "007", "008");
  public static final List<String> MANDATORY_FIELDS = Arrays.asList("000", "001", "008", "040");
  public static final List<String> AUT_NAMES = Arrays.asList("100", "110", "111");
  public static final List<String> AUT_NAMES_X = Arrays.asList("400", "410", "411", "500", "510", "511", "700", "710",
      "711");
  public static final List<String> AUT_TITLE = Arrays.asList("130");
  public static final List<String> AUT_TITLE_X = Arrays.asList("430", "530", "730");
  public static final List<String> AUT_SUBJECT = Arrays.asList("150", "151", "155", "180", "182", "185");
  public static final List<String> AUT_SUBJECT_X = Arrays.asList("450", "550", "750", "451", "551", "751", "455", "555",
      "755", "480", "580", "780", "482", "582", "782", "485", "585", "785");
  public static final List<String> NAMES = Arrays.asList("100", "110", "111");
  public static final List<String> NAMES_D = Arrays.asList("110", "111", "710", "711");
  public static final List<String> NAMES_E = Arrays.asList("100", "110", "120", "121", "240", "243", "400", "410",
      "600", "610", "700", "710", "720", "721", "800", "810", "900", "910", "980", "981");
  public static final List<String> NAMES_X = Arrays.asList("400", "410", "411", "700", "710", "711", "720", "721",
      "722", "900", "910", "911", "980", "981", "982");
  public static final List<String> NAMES_V = Arrays.asList("400", "410", "411", "800", "810", "811", "980", "981",
      "982");
  public static final List<String> NAMES_245 = Arrays.asList("600", "610", "611", "700", "710", "711", "720", "721",
      "722", "800", "810", "811", "900", "910", "911", "980", "981", "982");
  public static final List<String> SUBJECTS_4 = Arrays.asList("600", "610", "611");
  public static final List<String> SUBJECTS_E = Arrays.asList("600", "610");
  public static final List<String> TITLES_X = Arrays.asList("440", "730", "740", "930", "983");
  public static final List<String> TITLES_V = Arrays.asList("440", "830", "983");
  public static final List<String> TITLES = Arrays.asList("130", "241", "245", "730", "740", "830", "930", "941", "945",
      "983");
  public static final String TERMINAL_PUNCTUATION = ".?!)-";
  public static final String OTHER_TERMINAL_PUNCTUATION = ".?!)]-";
  public static final String SUBFIELD_DELIMITER = "\u001f";
  public static final Configuration HCONFIGURATION = new Configuration();
  public static final List<String> SKIP_IN_FILING_CODES = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8",
      "9");
  public static final String MODULE_CONFIGURATION = "mod-configuration";
  public static final String SUB_PATH_CONFIGURATION = "/configurations/entries";
  public static final String UNDEFINED = "und";
  public static final List<String> AUT_NOTES = Arrays.asList("667", "670", "672", "673", "675", "677", "678", "680",
      "681", "682", "688");

  public static final short SUBJECT_SOURCE_CODE_OTHERS = 6;

  static {
    Map<String, String> errors = new HashMap<>();
    errors.put(ERROR_MANDATORY_TAG, "Check mandatory TAGS failure.");
    errors.put(ERROR_DUPLICATE_TAG, "Duplicate TAGS for : %s");
    errors.put(ERROR_EMPTY_TAG, "Some TAGS appears empties: %s.");
    errors.put(NO_RECORD_FOUND, "Record not found: %d.");
    errors.put(NO_TAG_FOUND, "Tag %s not found");
    ERRORS_MAP = Collections.unmodifiableMap(errors);
  }

  static {
    Map<Integer, String> physical = new HashMap<>();
    physical.put(23, GLOBE);
    physical.put(24, MAP_CODE);
    physical.put(25, MICROFORM);
    physical.put(26, MOTION_PICTURE);
    physical.put(27, NON_PROJECTED_GRAPHIC);
    physical.put(28, PROJECTED_GRAPHIC);
    physical.put(29, SOUND_RECORDING);
    physical.put(30, VIDEO_RECORDING);
    physical.put(42, ELECTRONIC_RESOURCE);
    physical.put(43, REMOTE_SENSING_IMAGE);
    physical.put(44, TEXT_CODE);
    physical.put(45, UNSPECIFIED);
    physical.put(46, TACTILE_MATERIAL);
    physical.put(47, KIT_CODE);
    physical.put(48, NOTATED_MUSIC);
    PHYSICAL_TYPES_MAP = Collections.unmodifiableMap(physical);
  }

  static {
    Map<String, Class<?>> code = new HashMap<>();
    code.put("BOOK_MATERIAL_CODE", T_BOOK_TYP_CDE.class);
    code.put("MUSIC_MATERIAL_CODE", T_MSC_TYP_CDE.class);
    code.put("MAP_MATERIAL_CODE", T_CRTGC_TYP_CDE.class);
    code.put("VM_MATERIAL_CODE", T_VSL_TYP_CDE.class);
    code.put("DATE_TYPE", T_ITM_DTE_TYP.class);
    code.put("MODIFIED_RECORD_TYPE", T_REC_MDFTN.class);
    code.put("CATALOGUING_SOURCE", T_REC_CTLGG_SRC.class);
    code.put("BOOK_ILLUSTRATION", T_BOOK_ILSTN.class);
    code.put("TARGET_AUDIENCE", T_TRGT_AUDNC.class);
    code.put("FORM_OF_ITEM", T_FORM_OF_ITM.class);
    code.put("NATURE_OF_CONTENT", T_NTR_OF_CNTNT.class);
    code.put("GOV_PUBLICATION", T_GOVT_PBLTN.class);
    code.put("CONF_PUBLICATION", T_CONF_PBLTN.class);
    code.put("BOOK_FESTSCHRIFT", T_BOOK_FTSCT.class);
    code.put("BOOK_INDEX", T_BOOK_IDX_AVBTY.class);
    code.put("BOOK_LITERARY_FORM", T_BOOK_LTRY_FORM_TYP.class);
    code.put("BOOK_BIOGRAPHY", T_BOOK_BGPHY.class);
    code.put("MSC_FORM_OF_COMPOSITION", T_MSC_FORM_OR_TYP.class);
    code.put("MSC_FORMAT", T_MSC_FRMT.class);
    code.put("MSC_PARTS", T_MSC_PRT.class);
    code.put("MSC_TEXTUAL_MAT_CODE", T_MSC_TXTL_MTR.class);
    code.put("MSC_LITERARY_TEXT", T_MSC_LTRY_TXT.class);
    code.put("MSC_TRANSPOSITION_CODE", T_MSC_TRNSPSN_ARRNGMNT.class);
    code.put("SRL_FREQUENCY", T_SRL_FREQ.class);
    code.put("SRL_REGULARITY", T_SRL_REGTY.class);
    code.put("SRL_TYPE_CONTINUING_RESOURCE", T_SRL_TYP.class);
    code.put("SRL_FORM_ORGNL_ITEM", T_SRL_FORM_ORGNL_ITM.class);
    code.put("SRL_NATURE_OF_WORK", T_NTR_OF_CNTNT.class);
    code.put("SRL_ORIGIN_ALPHABET", T_SRL_TTL_ALPBT.class);
    code.put("SRL_ENTRY_CONVENTION", T_SRL_SCSV_LTST.class);
    code.put("MAP_RELIEF", T_CRTGC_RLF.class);
    code.put("MAP_PROJECTION", T_CRTGC_PRJTN.class);
    code.put("MAP_TYPE_MATERIAL", T_CRTGC_MTRL.class);
    code.put("MAP_INDEX", T_CRTGC_IDX_AVBTY.class);
    code.put("MAP_SPECIAL_FORMAT_CHARACTERISTIC", T_CRTGC_FRMT.class);
    code.put("VSL_TARGET_AUDIENCE", T_VSL_TRGT_AUDNC.class);
    code.put("VSL_TYPE_MATERIAL", T_VSL_MTRL_TYP.class);
    code.put("VSL_TECHNIQUE", T_VSL_TECH.class);
    code.put("COMPUTER_TARGET_AUDIENCE", T_CMPTR_TRGT_AUDNC.class);
    code.put("COMPUTER_FORM_OF_ITEM", T_CF_FORM_OF_ITM.class);
    code.put("COMPUTER_TYPE_MATERIAL", T_CMPTR_FIL_TYP.class);
    code.put("MARC_COUNTRY", T_MARC_CNTRY.class);
    code.put("LANGUAGE", T_LANG.class);
    // 007
    code.put("CATEGORY_MATERIAL", GeneralMaterialDesignation.class);
    code.put("SOUND_MEDIUM_OR_SEP", T_SND_MDM_OR_SEPRT.class);
    code.put("MEDIUM_FOR_SOUND", T_MDM_FOR_SND.class);
    code.put("MAP_SPEC_DESIGN", T_MAP_SMD.class);
    code.put("MAP_COLOR", T_MAP_CLR.class);
    code.put("MAP_PHYSICAL_MEDIUM", T_MAP_PHSCL_MDM.class);
    code.put("MAP_TYPE_OF_REPRODUCTION", T_MAP_RPRDT_TYP.class);
    code.put("MAP_PRODUCTION_DETAILS", T_MAP_PRDTN_DTL.class);
    code.put("MAP_POLARITY", T_MAP_PLRTY.class);
    code.put("CF_SPEC_DESIGN", T_CF_SMD.class);
    code.put("CF_COLOR", T_CF_CLR.class);
    code.put("CF_DIMENSIONS", T_CF_DMNSN.class);
    code.put("CF_FILE_FORMAT", T_CF_FF.class);
    code.put("CF_QUALITY_ASS", T_CF_QAT.class);
    code.put("CF_ANTECEDENT_SRC", T_CF_ANTSRC.class);
    code.put("CF_COMPRESSION_LVL", T_CF_LOC.class);
    code.put("CF_REFORMATTING_QUALITY", T_CF_RQ.class);
    code.put("GLB_SPEC_DESIGN", T_GLB_SMD.class);
    code.put("GLB_COLOR", T_GLB_CLR.class);
    code.put("GLB_PHYSICAL_MEDIUM", T_GLB_PHSCL_MDM.class);
    code.put("GLB_TYPE_OF_REPRODUCTION", T_GLB_RPDTN_TYP.class);
    code.put("TCT_SPEC_DESIGN", T_TM_SMD.class);
    code.put("TCT_CLASS_BRAILLE_WRITING", T_TM_CBW.class);
    code.put("TCT_CONTRACTION_LVL", T_TM_LC.class);
    code.put("TCT_BRAILLE_MUSIC_FORMAT", T_TM_BMF.class);
    code.put("TCT_SPECIAL_PHYSICAL_CHAR", T_TM_SPC.class);
    code.put("PG_SPEC_DESIGN", T_PG_SMD.class);
    code.put("PG_COLOR", T_PG_CLR.class);
    code.put("PG_EMUL_BASE", T_PG_BSE_OF_EMLSN_MTRL.class);
    code.put("PG_DIMENSIONS", T_PG_DMNSN.class);
    code.put("PG_SECONDARY_SUPPORT", T_PG_SCDRY_SPRT_MTRL.class);
    code.put("NPG_SPEC_DESIGN", T_NPG_SMD.class);
    code.put("NPG_COLOR", T_NPG_CLR.class);
    code.put("NPG_PRIMARY_SUPPORT", T_NPG_PRMRY_SPRT_MTRL.class);
    code.put("NPG_SECONDARY_SUPPORT", T_NPG_SCDRY_SPRT_MTRL.class);
    code.put("MP_SPEC_DESIGN", T_MP_SMD.class);
    code.put("MP_COLOR", T_MP_CLR.class);
    code.put("MP_PRESENT_FORMAT", T_MP_PRSTN_FRMT.class);
    code.put("MP_DIMENSIONS", T_MP_DMNSN.class);
    code.put("MP_CONF_PLAYBACK", T_MP_CONFIG.class);
    code.put("MP_PROD_ELEM", T_MP_PROD_ELEM.class);
    code.put("MP_POLARITY", T_MP_POS_NEG.class);
    code.put("MP_GENERATION", T_MP_GNRTN.class);
    code.put("MP_BASE_FILM", T_MP_BSE_FLM.class);
    code.put("MP_REFINE_CAT_COLOR", T_MP_RF_CLR.class);
    code.put("MP_KIND_COLORS", T_MP_CLR_STCK.class);
    code.put("MP_DETERIORATION_STAGE", T_MP_DTRTN_STGE.class);
    code.put("MP_COMPLETENESS", T_MP_CMPLT.class);
    code.put("KIT_SPEC_DESIGN", T_KIT_SMD.class);
    code.put("NMU_SPEC_DESIGN", T_NM_SMD.class);
    code.put("TXT_SPEC_DESIGN", T_TXT_SMD.class);
    code.put("UNS_SPEC_DESIGN", T_USP_SMD.class);
    code.put("RSI_SPEC_DESIGN", T_RSI_SMD.class);
    code.put("RSI_ALTITUDE", T_RSI_ALT_SENS.class);
    code.put("RSI_ATTITUDE", T_RSI_ATT_SENS.class);
    code.put("RSI_CLOUD_COVER", T_RSI_CLD_CVR.class);
    code.put("RSI_PLAT_CONSTRUCTION", T_RSI_PLTFRM_CNSTRCT.class);
    code.put("RSI_PLAT_USE", T_RSI_PLTFRM_USE.class);
    code.put("RSI_SENSOR_TYPE", T_RSI_SNSR_TPE.class);
    code.put("RSI_DATA_TYPE", T_RSI_DATA_TPE.class);
    code.put("SND_SPEC_DESIGN", T_SND_SMD.class);
    code.put("SND_SPEED", T_SND_SPD.class);
    code.put("SND_CONF_PLAYBACK", T_SND_PLYBC_CHNL_CFGTN.class);
    code.put("SND_GROOVE_WIDTH", T_SND_DISC_GRV_WDTH.class);
    code.put("SND_DIMENSIONS", T_SND_DMNSN.class);
    code.put("SND_TAPE_WIDTH", T_SND_TAPE_WDTH.class);
    code.put("SND_TAPE_CONF", T_SND_TAPE_CFGTN.class);
    code.put("SND_DISC_TYPE", T_SND_DISC_CYLND_TYP.class);
    code.put("SND_MATERIAL_TYPE", T_SND_MTRL_TYP.class);
    code.put("SND_CUTTING", T_SND_DISC_CTG.class);
    code.put("SND_SPEC_PLAYBACK", T_SND_SPCL_PLYBC_CHAR.class);
    code.put("SND_STORAGE_TECNIQUE", T_SND_STRG_TECH.class);
    code.put("VR_SPEC_DESIGN", T_VR_SMD.class);
    code.put("VR_COLOR", T_VR_CLR.class);
    code.put("VR_FORMAT", T_VR_FRMT.class);
    code.put("VR_DIMENSIONS", T_VR_DMNSN.class);
    code.put("VR_CONF_PLAYBACK", T_VR_PLYBC_CHNL_CFGTN.class);
    code.put("MIC_COLOR", T_MIC_CLR.class);
    code.put("MIC_DIMENSIONS", T_MIC_DMNSN.class);
    code.put("MIC_BASE_FILM", T_MIC_FLM_BSE.class);
    code.put("MIC_EMUL_FILM", T_MIC_FLM_EMLSN.class);
    code.put("MIC_GENERATION", T_MIC_GNRTN.class);
    code.put("MIC_POLARITY", T_MIC_PLRTY.class);
    code.put("MIC_REDUCT_RATIO_RANGE", T_MIC_RDCTN_RATIO_RNG.class);
    code.put("MIC_SPEC_DESIGN", T_MIC_SMD.class);
    // Authority
    code.put("SUBJECT_DESCRIPTOR", T_AUT_SBJCT_DSCTR.class);
    code.put("ROMANIZATION_SCHEME", T_AUT_RMNZT_SCHM.class);
    code.put("BILINGUAL_USAGE", T_AUT_BLNGL_USG.class);
    code.put("AUT_RECORD_TYPE", T_AUT_REC_TYP.class);
    code.put("AUT_CATALOGUIG_RULES", T_AUT_CTLGG_RLE.class);
    code.put("SUBJECT_SYSTEM", T_AUT_SBJCT_SYS.class);
    code.put("SERIES_TYPE", T_AUT_SRS_TYP.class);
    code.put("SERIES_NUMBERING", T_AUT_SRS_NBRG.class);
    code.put("MAIN_ADDED_ENTRY_INDICATOR", T_AUT_MAIN_ADD_ENTRY.class);
    code.put("SUBJECT_ENTRY_INDICATOR", T_AUT_SBJCT_ENTRY.class);
    code.put("SERIES_ENTRY_INDICATOR", T_AUT_SRS_ENTRY.class);
    code.put("SUB_DIVISION_TYPE", T_AUT_SUB_DIV_TYP.class);
    code.put("GOVERNMENT_AGENCY", T_AUT_GOVT_AGNCY.class);
    code.put("REFERENCE_STATUS", T_AUT_REF_STUS.class);
    code.put("AUT_RECORD_REVISION", T_AUT_REC_RVSN.class);
    code.put("NON_UNIQUE_NAME", T_AUT_NON_UNQ_NME.class);
    code.put("HEADING_STATUS", T_AUT_HDG_STUS.class);
    code.put("RECORD_MODIFICATION", T_AUT_REC_MDFTN.class);
    code.put("CATALOGUING_SOURCE_CODE", T_AUT_CTLGG_SRC.class);

    MAP_CODE_LISTS = Collections.unmodifiableMap(code);
  }

  static {
    Map<String, Class<?>> bibliographicAccessPoint = new HashMap<>();
    bibliographicAccessPoint.put("NH", NameAccessPoint.class);
    bibliographicAccessPoint.put("TH", TitleAccessPoint.class);
    bibliographicAccessPoint.put("SH", SubjectAccessPoint.class);
    bibliographicAccessPoint.put("MH", NameTitleAccessPoint.class);
    BIBLIOGRAPHIC_ACCESS_POINT_CLASS_MAP = Collections.unmodifiableMap(bibliographicAccessPoint);
  }

  static {
    Map<String, String> indexAuthority = new HashMap<>();
    indexAuthority.put("NH", "NK");
    indexAuthority.put("TH", "TK");
    indexAuthority.put("SH", "SK");
    indexAuthority.put("MH", "NTK");
    INDEX_AUTHORITY_TYPE_MAP = Collections.unmodifiableMap(indexAuthority);
  }

  static {
    Map<String, Class<?>> daoMap = new HashMap<>();
    daoMap.put("2P0", NameDescriptorDAO.class);
    daoMap.put("3P10", NameDescriptorDAO.class);
    daoMap.put("4P10", NameDescriptorDAO.class);
    daoMap.put("5P10", NameDescriptorDAO.class);
    daoMap.put("7P0", TitleDescriptorDAO.class);
    daoMap.put("9P0", SubjectDescriptorDAO.class);
    daoMap.put("230P", PublisherNameDescriptorDAO.class);
    daoMap.put("243P", PublisherPlaceDescriptorDAO.class);
    daoMap.put("250S", NameTitleNameDescriptorDAO.class);
    daoMap.put("251S", NameTitleTitleDescriptorDAO.class);
    daoMap.put("16P30", ControlNumberDescriptorDAO.class);
    daoMap.put("18P2", ControlNumberDescriptorDAO.class);
    daoMap.put("19P2", ControlNumberDescriptorDAO.class);
    daoMap.put("20P3", ControlNumberDescriptorDAO.class);
    daoMap.put("21P2", ControlNumberDescriptorDAO.class);
    daoMap.put("22P10", ControlNumberDescriptorDAO.class);
    daoMap.put("29P20", ControlNumberDescriptorDAO.class);
    daoMap.put("30P4", ControlNumberDescriptorDAO.class);
    daoMap.put("31P3", ControlNumberDescriptorDAO.class);
    daoMap.put("32P3", ControlNumberDescriptorDAO.class);
    daoMap.put("33P3", ControlNumberDescriptorDAO.class);
    daoMap.put("34P20", ControlNumberDescriptorDAO.class);
    daoMap.put("35P20", ControlNumberDescriptorDAO.class);
    daoMap.put("36P20", ControlNumberDescriptorDAO.class);
    daoMap.put("51P3", ControlNumberDescriptorDAO.class);
    daoMap.put("52P3", ControlNumberDescriptorDAO.class);
    daoMap.put("53P3", ControlNumberDescriptorDAO.class);
    daoMap.put("54P3", ControlNumberDescriptorDAO.class);
    daoMap.put("55P3", ControlNumberDescriptorDAO.class);
    daoMap.put("47P40", ClassificationDescriptorDAO.class);
    daoMap.put("24P5", ClassificationDescriptorDAO.class);
    daoMap.put("25P5", ClassificationDescriptorDAO.class);
    daoMap.put("27P5", ClassificationDescriptorDAO.class);
    daoMap.put("23P5", ClassificationDescriptorDAO.class);
    daoMap.put("48P3", ClassificationDescriptorDAO.class);
    daoMap.put("46P40", ClassificationDescriptorDAO.class);
    daoMap.put("50P3", ClassificationDescriptorDAO.class);
    daoMap.put("49P3", ClassificationDescriptorDAO.class);
    daoMap.put("326P1", ClassificationDescriptorDAO.class);
    daoMap.put("353P1", ClassificationDescriptorDAO.class);
    daoMap.put("303P3", ClassificationDescriptorDAO.class);
    daoMap.put("28P30", ShelfListDAO.class);
    daoMap.put("244P30", ShelfListDAO.class);
    daoMap.put("47P30", ShelfListDAO.class);
    daoMap.put("37P30", ShelfListDAO.class);
    daoMap.put("38P30", ShelfListDAO.class);
    daoMap.put("39P30", ShelfListDAO.class);
    daoMap.put("41P30", ShelfListDAO.class);
    daoMap.put("42P30", ShelfListDAO.class);
    daoMap.put("43P30", ShelfListDAO.class);
    daoMap.put("44P30", ShelfListDAO.class);
    daoMap.put("45P30", ShelfListDAO.class);
    daoMap.put("46P30", ShelfListDAO.class);
    daoMap.put("373P0", SubjectDescriptorDAO.class);
    DAO_CLASS_MAP = Collections.unmodifiableMap(daoMap);
  }

  static {
    Map<String, String> filter = new HashMap<>();
    filter.put("2P0", "");
    filter.put("3P10", " and hdg.typeCode = 2 ");
    filter.put("4P10", " and hdg.typeCode = 3 ");
    filter.put("5P10", " and hdg.typeCode = 4 ");
    filter.put("7P0", "");
    filter.put("9P0", "");
    filter.put("230P", "");
    filter.put("243P", "");
    filter.put("250S", "");
    filter.put("251S", "");
    filter.put("16P30", "");
    filter.put("18P2", " and hdg.typeCode = 9 ");
    filter.put("19P2", " and hdg.typeCode = 10 ");
    filter.put("20P3", " and hdg.typeCode = 93 ");
    filter.put("21P2", " and hdg.typeCode = 2 ");
    filter.put("22P10", " and hdg.typeCode = 93 ");
    filter.put("29P20", " and hdg.typeCode = 71 ");
    filter.put("30P4", "");
    filter.put("31P3", " and hdg.typeCode = 84 ");
    filter.put("32P3", " and hdg.typeCode = 88 ");
    filter.put("33P3", " and hdg.typeCode = 90 ");
    filter.put("34P20", "");
    filter.put("35P20", "");
    filter.put("36P20", " and hdg.typeCode = 52 ");
    filter.put("51P3", " and hdg.typeCode = 89 ");
    filter.put("52P3", " and hdg.typeCode = 83 ");
    filter.put("53P3", " and hdg.typeCode = 91 ");
    filter.put("54P3", " and hdg.typeCode = 97 ");
    filter.put("55P3", " and hdg.typeCode = 98 ");
    filter.put("47P40", " and hdg.typeCode = 21");
    filter.put("24P5", " and hdg.typeCode = 12");
    filter.put("25P5", " and hdg.typeCode = 1");
    filter.put("27P5", " and hdg.typeCode = 6");
    filter.put("23P5", " and hdg.typeCode not in (1,6,10,11,12,14,15,29) ");
    filter.put("48P3", " and hdg.typeCode = 10");
    filter.put("46P40", " and hdg.typeCode = 11");
    filter.put("50P3", " and hdg.typeCode = 14");
    filter.put("49P3", " and hdg.typeCode = 15");
    filter.put("326P1", " and hdg.typeCode = 29");
    filter.put("28P30", " and hdg.typeCode = '@'");
    filter.put("244P30", " and hdg.typeCode = 'N'");
    filter.put("47P30", " and hdg.typeCode = 'M'");
    filter.put("37P30", " and hdg.typeCode = '2'");
    filter.put("38P30", " and hdg.typeCode = '3'");
    filter.put("39P30", " and hdg.typeCode = '4'");
    filter.put("41P30", " and hdg.typeCode = '6'");
    filter.put("42P30", " and hdg.typeCode = 'A'");
    filter.put("43P30", " and hdg.typeCode = 'C'");
    filter.put("44P30", " and hdg.typeCode = 'E'");
    filter.put("45P30", " and hdg.typeCode = 'F'");
    filter.put("46P30", " and hdg.typeCode = 'Global'");
    filter.put("303P3", " and hdg.typeCode = 13");
    filter.put("354P0", "");
    filter.put("353P1", " and hdg.typeCode = 80");
    filter.put("373P0", " and hdg.sourceCode = 4 ");
    FILTER_MAP = Collections.unmodifiableMap(filter);
  }

  static {
    HCONFIGURATION.setProperty("hibernate.dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
    HCONFIGURATION.setProperty("dialect", "net.sf.hibernate.dialect.PostgreSQLDialect");
    HCONFIGURATION.setProperty("show_sql", System.getProperty("show.sql", "false"));
    try {
      HCONFIGURATION.configure("/hibernate.cfg.xml");
    } catch (final Exception failure) {
      throw new ExceptionInInitializerError(failure);
    }
  }

  private Global() {
    throw new IllegalStateException("Global class");
  }
}
