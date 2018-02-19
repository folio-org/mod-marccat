package org.folio.cataloging.log;

public interface MessageCatalog {
    String MODULE_NAME = "MODCAT";

    String _00010_DATA_ACCESS_FAILURE = "<" + MODULE_NAME + "-00010> : Data access failure. Please check the stacktrace below for further information.";
    String _00011_NWS_FAILURE = "<" + MODULE_NAME + "-00011> : Not well known failure. Please check the stacktrace below for further information.";
    String _00012_NULL_RESULT = "<" + MODULE_NAME + "-00012> : Not well known failure. The service did return a nullable (not allowed) value.";
    String _00013_IO_FAILURE = "<" + MODULE_NAME + "-00013> : I/O failure. Please check the stacktrace below for further details.";

    String _00014_NO_VALIDATION_FOUND = "<" + MODULE_NAME + "-00014> : Not well known failure. No validation found for category %s and values %s.";
}
