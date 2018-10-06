package org.folio.cataloging.search;

import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.search.engine.SearchEngine;
import org.folio.cataloging.search.engine.impl.DefaultModCatalogingSearchEngine;
import org.folio.cataloging.search.engine.impl.LightweightModCatalogingSearchEngine;

/**
 * A search engine factory.
 *
 * @author agazzarini
 * @since 1.0
 */
public abstract class SearchEngineFactory {
  /**
   * Creates a search engine reference using the given data.
   *
   * @param type                    the search engine type.
   * @param mainLibraryId           the main library identifier.
   * @param databasePreferenceOrder the database preference order.
   * @param service                 the storage service.
   * @return a {@link SearchEngine} instance.
   */
  public static SearchEngine create(final SearchEngineType type, final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
    switch (type) {
      case LIGHTWEIGHT:
        return new LightweightModCatalogingSearchEngine (mainLibraryId, databasePreferenceOrder, service);
      default:
        return new DefaultModCatalogingSearchEngine (mainLibraryId, databasePreferenceOrder, service);
    }
  }
}
