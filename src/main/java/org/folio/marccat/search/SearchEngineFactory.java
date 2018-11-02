package org.folio.marccat.search;

import org.folio.marccat.integration.StorageService;
import org.folio.marccat.search.engine.SearchEngine;
import org.folio.marccat.search.engine.impl.DefaultModCatalogingSearchEngine;
import org.folio.marccat.search.engine.impl.LightweightModCatalogingSearchEngine;
import org.folio.marccat.search.engine.impl.LightweightVerticalModCatalogingSearchEngine;

/**
 * A search engine factory.
 *
 * @author cchiama
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
  public static SearchEngine create(final EngineType type, final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
    switch (type) {
      case LIGHTWEIGHT:
        return new LightweightModCatalogingSearchEngine(mainLibraryId, databasePreferenceOrder, service);
      case LIGHTWEIGHT_VERTICAL:
        return new LightweightVerticalModCatalogingSearchEngine(mainLibraryId, databasePreferenceOrder, service);
      default:
        return new DefaultModCatalogingSearchEngine(mainLibraryId, databasePreferenceOrder, service);
    }
  }

  public enum EngineType {
    DEFAULT, LIGHTWEIGHT, LIGHTWEIGHT_VERTICAL
  }
}
