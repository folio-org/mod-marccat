package org.folio.cataloging.log;

public interface MessageCatalog {
    String MODULE_NAME = "MODCAT";

    String _00010_DATA_ACCESS_FAILURE = "<" + MODULE_NAME + "-00010> : Data access failure. Please check the stacktrace below for further information.";
    String _00011_NWS_FAILURE = "<" + MODULE_NAME + "-00011> : Not well known failure. Please check the stacktrace below for further information.";
}
