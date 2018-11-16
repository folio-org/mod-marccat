package org.folio.marccat.log;

//TODO: delete messages related to storage and dao
public interface MessageCatalog {
  String MODULE_NAME = "MODCAT";
  String _00010_DATA_ACCESS_FAILURE = "<" + MODULE_NAME + "-00010> : Data access failure. Please check the stacktrace below for further information.";
  String _00011_NWS_FAILURE = "<" + MODULE_NAME + "-00011> : Not well known failure. Please check the stacktrace below for further information.";
  String _00012_NULL_RESULT = "<" + MODULE_NAME + "-00012> : Not well known failure. The service did return a nullable (not allowed) stringValue.";
  String _00013_IO_FAILURE = "<" + MODULE_NAME + "-00013> : I/O failure. Please check the stacktrace below for further details.";
  String _00014_NO_VALIDATION_FOUND = "<" + MODULE_NAME + "-00014> : Not well known failure. No validation found for category %s and values %s.";
  String _00016_NO_HEADING_FOUND = "<" + MODULE_NAME + "-00016> : No heading found for heading number: %s";
  String _00017_MARC_CORRELATION_SORTING = "<" + MODULE_NAME + "-00017> : Marc correlation exception. ErrorCollection during sort tags.";
  String _00018_CANNOT_CREATE = "<" + MODULE_NAME + "-00018> : Unable to create the requested entity.";
  String _00020_SE_QUERY = "<" + MODULE_NAME + "-00020> : CCL => \"%s\" became \"%s\"";
  String _00021_UNABLE_TO_PARSE_RECORD_DATA = "<" + MODULE_NAME + "-00021> : Invalid XML record data %s";
  String _00023_SE_REQRES = "<" + MODULE_NAME + "-00023> : CCL => \"%s\", %s matches.";
  String _00024_XSLT_FAILURE = "<" + MODULE_NAME + "-00024> : XSLT failure. Please check the stacktrace below for further details.";

}
