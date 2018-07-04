package org.folio.cataloging.search;

import org.folio.cataloging.business.searching.SearchEngine;
import org.folio.cataloging.integration.StorageService;

/**
 * A search engine factory.
 *
 * @author agazzarini
 * @since 1.0
 */
public abstract class SearchEngineFactory {

    public SearchEngine create(final SearchEngineType type, final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
        switch(type) {
            case LIGHTWEIGHT: return new LIghtweightModCatalogingSearchEngine(mainLibraryId, databasePreferenceOrder, service);
            default: return new DefaultModCatalogingSearchEngine(mainLibraryId, databasePreferenceOrder, service);
        }
    }
}