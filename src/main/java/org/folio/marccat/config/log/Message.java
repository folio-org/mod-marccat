package org.folio.marccat.config.log;

public final class Message {

  private Message() {
  }

  public static final String MODULE_NAME = "MODCAT";

  public static final String MOD_MARCCAT_00010_DATA_ACCESS_FAILURE = "<" + MODULE_NAME
      + "-00010> : Data access failure. Please check the stacktrace below for further information.";
  public static final String MOD_MARCCAT_00011_NWS_FAILURE = "<" + MODULE_NAME
      + "-00011> : Not well known failure. Please check the stacktrace below for further information.";
  public static final String MOD_MARCCAT_00012_NULL_RESULT = "<" + MODULE_NAME
      + "-00012> : Not well known failure. The service did return a nullable (not allowed) stringValue.";
  public static final String MOD_MARCCAT_00013_IO_FAILURE = "<" + MODULE_NAME
      + "-00013> : I/O failure. Please check the stacktrace below for further details.";
  public static final String MOD_MARCCAT_00014_NO_VALIDATION_FOUND = "<" + MODULE_NAME
      + "-00014> : Not well known failure. No validation found for category %s and values %s.";
  public static final String MOD_MARCCAT_00015_NO_VALIDATION_FOUND = "<" + MODULE_NAME
      + "-00015> : Not well known failure. No validation found for category %s and values %s.";
  public static final String MOD_MARCCAT_00016_NO_HEADING_FOUND = "<" + MODULE_NAME
      + "-00016> : No heading found for heading number: %s";
  public static final String MOD_MARCCAT_00016_FIELD_PARAMETER_INVALID = "<" + MODULE_NAME
      + "-00016> : Unable to retrieve field template for invalid parameters. Category %s and code %s.";
  public static final String MOD_MARCCAT_00017_CODES_GROUPS_NOT_AVAILABLE = "<" + MODULE_NAME
      + "-00017> : No codes groups available for this tag/field. Code %s.";
  public static final String MOD_MARCCAT_00017_MARC_CORRELATION_SORTING = "<" + MODULE_NAME
      + "-00017> : Marc correlation exception. ErrorCollection during sort tags.";
  public static final String MOD_MARCCAT_00018_CANNOT_CREATE = "<" + MODULE_NAME
      + "-00018> : Unable to create the requested entity.";
  public static final String MOD_MARCCAT_00018_NO_HEADING_TYPE_CODE = "<" + MODULE_NAME
      + "-00018> : No heading type code selected for tag %s.";
  public static final String MOD_MARCCAT_00019_HEADER_TYPE_ID_WRONG = "<" + MODULE_NAME
      + "-00019> : The <<headerTypeCode>> is wrong or null. Unable to retrieve codes groups for tag/field %s.";
  public static final String MOD_MARCCAT_00019_SAVE_RECORD_FAILURE = "<" + MODULE_NAME
      + "-00019> : Error during save or update record %d.";
  public static final String MOD_MARCCAT_00019_SAVE_AUT_RECORD_FAILURE = "<" + MODULE_NAME
      + "-00019> : Error during save or update authority record %d.";
  public static final String MOD_MARCCAT_00119_DAO_CLASS_MAP_NOT_FOUND = "<" + MODULE_NAME
      + "-00119> : Unable to retrieve the class by key. Key %s.";
  public static final String MOD_MARCCAT_00020_SE_QUERY = "<" + MODULE_NAME + "-00020> : CCL => \"%s\" became \"%s\"";
  public static final String MOD_MARCCAT_00020_LOCK_FAILURE = "<" + MODULE_NAME
      + "-00020> : Lock record failure. Record already in use (-id:%d -username:%s).";
  public static final String MOD_MARCCAT_00021_UNLOCK_FAILURE = "<" + MODULE_NAME
      + "-00021> : Unlock record failure (-id:%d -username:%s).";
  public static final String MOD_MARCCAT_00021_UNABLE_TO_PARSE_RECORD_DATA = "<" + MODULE_NAME
      + "-00021> : Invalid XML record data %s";
  public static final String MOD_MARCCAT_00022_DELETE_RECORD_FAILURE = "<" + MODULE_NAME
      + "-00022> : Error during record delete %d.";
  public static final String MOD_MARCCAT_00023_SAVE_TEMPLATE_ASSOCIATED_FAILURE = "<" + MODULE_NAME
      + "-00023> : Error during save or update record template %d, associated to record %d.";
  public static final String MOD_MARCCAT_00023_SE_REQRES = "<" + MODULE_NAME + "-00023> : CCL => \"%s\", %s matches.";
  public static final String MOD_MARCCAT_00024_XSLT_FAILURE = "<" + MODULE_NAME
      + "-00024> : XSLT failure. Please check the stacktrace below for further details.";
  public static final String MOD_MARCCAT_00025_DUPLICATE_TAG = "<" + MODULE_NAME + "-00025> : Duplicate tag for %s.";
  public static final String MOD_MARCCAT_00026_MANDATORY_FAILURE = "<" + MODULE_NAME
      + "-00026> : Check mandatory tags failure. Record ID %d.";
  public static final String MOD_MARCCAT_00027_EMPTY_TAG = "<" + MODULE_NAME + "-00027> : Some tags appears empty %s.";
  public static final String MOD_MARCCAT_00030_LOAD_RECORDS_FAILURE = "<" + MODULE_NAME
      + "-00030> : Error during records loading.";
  public static final String MOD_MARCCAT_00031_LOAD_FROM_FILE_FAILURE = "<" + MODULE_NAME
      + "-00031> : Error during load from file procedure. File %s not loaded.";
  public static final String MOD_MARCCAT_00032_LOAD_REC_BY_REC_FAILURE = "<" + MODULE_NAME
      + "-00032> : Error during load record from file procedure. Record %s not loaded.";
  public static final String MOD_MARCCAT_00033_PROCESS_FAILURE = "<" + MODULE_NAME
      + "-00033> : Error during wait process";
  public static final String MOD_MARCCAT_00034_CLIENT_FAILURE = "<" + MODULE_NAME
      + "-00034> : Client connection refused";
  public static final String MOD_MARCCAT_00035_TAG_NOT_FOUND_FAILURE = "<" + MODULE_NAME + "-00035> : Tag %s not found";
  public static final String MOD_MARCCAT_00036_NOT_CREATE_OBJECT = "<" + MODULE_NAME
      + "-00036> : Could not create object.";
  public static final String MOD_MARCCAT_00037_NOT_AUT_HEADING_TAG = "<" + MODULE_NAME
      + "-00037> : Error creating new Authority heading tag.";
  public static final String MOD_MARCCAT_00038_NOT_AUT_FOUND = "<" + MODULE_NAME + "-00038> : No authority found %s.";
  public static final String MOD_MARCCAT_00039_RECORD_USED = "<" + MODULE_NAME + "-00039> : Record in use %s.";

}
