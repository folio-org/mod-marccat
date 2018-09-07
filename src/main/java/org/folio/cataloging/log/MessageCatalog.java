package org.folio.cataloging.log;

//TODO: delete messages related to storage and dao
public interface MessageCatalog {
    String MODULE_NAME = "MODCAT";

    String _00010_DATA_ACCESS_FAILURE = "<" + MODULE_NAME + "-00010> : Data access failure. Please check the stacktrace below for further information.";
    String _00011_NWS_FAILURE = "<" + MODULE_NAME + "-00011> : Not well known failure. Please check the stacktrace below for further information.";
    String _00012_NULL_RESULT = "<" + MODULE_NAME + "-00012> : Not well known failure. The service did return a nullable (not allowed) stringValue.";
    String _00013_IO_FAILURE = "<" + MODULE_NAME + "-00013> : I/O failure. Please check the stacktrace below for further details.";

    String _00015_CFG_KEY_FAILURE = "<" + MODULE_NAME + "-00015> : Unable to retrieve the configuration attribute associated with >%s< key. Please check the stacktrace below for further details.";

    String _00016_FIELD_PARAMETER_INVALID = "<" + MODULE_NAME + "-00016> : Unable to retrieve field template for invalid parameters. Category %s and code %s.";
    String _00017_CODES_GROUPS_NOT_AVAILABLE = "<" + MODULE_NAME + "-00017> : No codes groups available for this tag/field. Code %s.";
    String _00018_CANNOT_CREATE= "<" + MODULE_NAME + "-00018> : Unable to create the requested entity.";

    String _00019_HEADER_TYPE_ID_WRONG = "<" + MODULE_NAME + "-00019> : The <<headerTypeCode>> is wrong or null. Unable to retrieve codes groups for tag/field %s.";
    String _00020_SE_QUERY = "<" + MODULE_NAME + "-00020> : CCL => \"%s\" became \"%s\"";
    String _00021_UNABLE_TO_PARSE_RECORD_DATA = "<" + MODULE_NAME + "-00021> : Invalid XML record data %s";
    String _00023_SE_REQRES = "<" + MODULE_NAME + "-00023> : CCL => \"%s\", %s matches.";
    String _00024_XSLT_FAILURE = "<" + MODULE_NAME + "-00024> : XSLT failure. Please check the stacktrace below for further details.";

    String _00025_DUPLICATE_TAG = "<" + MODULE_NAME + "-00025> : Duplicate tag for %s.";
    String _00026_MANDATORY_FAILURE = "<" + MODULE_NAME + "-00026> : Check mandatory tags failure. Record ID %d.";
    String _00027_EMPTY_TAG = "<" + MODULE_NAME + "-00027> : Some tags appears empty %s.";

}
