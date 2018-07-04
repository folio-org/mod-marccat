package org.folio.cataloging.search.engine.impl;

import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.search.XmlRecord;
import org.folio.cataloging.search.domain.Record;
import org.folio.cataloging.search.engine.ModCatalogingSearchEngine;

/**
 * ModCataloging Search Engine.
 * 
 * @author agazzarini
 * @since 1.0
 */
public class DefaultModCatalogingSearchEngine extends ModCatalogingSearchEngine {
	/**
	 * Builds a new Search engine instance with the given data.
	 *
	 * @param mainLibraryId           the main library identifier.
	 * @param databasePreferenceOrder the database preference order.
	 * @param service                 the {@link StorageService} instance.
	 */
	public DefaultModCatalogingSearchEngine(final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
		super(mainLibraryId, databasePreferenceOrder, service);
	}

	@Override
	public Record newRecord() {
		return new XmlRecord();
	}
}