package org.folio.cataloging.integration.log;

/**
 * Storage layer Message Catalog.
 *
 * @author nbianchini
 * @author agazzarini
 * @since 1.0
 */
public interface MessageCatalogStorage {
    String MODULE_NAME = "MODCAT-STORAGE";

    String _00010_DATA_ACCESS_FAILURE = "<" + MODULE_NAME + "-00010> : Data access failure. Please check the stacktrace below for further information.";
    String _00011_CACHE_UPDATE_FAILURE = "<" + MODULE_NAME + "-00011> : An error occurred during execution of AMICUS.CFN_PR_CACHE_UPDATE procedure. Error code: %d.";
    String _00012_TARGET_DESCRIPTOR_NULL = "<" + MODULE_NAME + "-00012> : Target descriptor is null. Can not transfer an heading to another.";
    String _00013_DIFFERENT_TARGET_SOURCE = "<" + MODULE_NAME + "-00013> : Target and Source descriptor are different classes. Can not transfer an heading to another.";
    String _00014_NO_VALIDATION_FOUND = "<" + MODULE_NAME + "-00014> : Not well known failure. No validation found for category %s and values %s.";
    String _00016_NO_HEADING_FOUND = "<" + MODULE_NAME + "-00016> : No heading found for heading number: %s";
    String _00017_MARC_CORRELATION_SORTING = "<" + MODULE_NAME + "-00017> : Marc correlation exception. Error during sort tags.";

}
