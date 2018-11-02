package org.folio.marccat.search.engine.impl;

import org.folio.marccat.exception.ModCatalogingException;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.search.domain.LightweightVerticalRecord;
import org.folio.marccat.search.domain.Record;
import org.folio.marccat.search.engine.ModCatalogingSearchEngine;

/**
 * ModMarccat Search Engine.
 *
 * @author cchiama
 * @since 1.0
 */
public class LightweightVerticalModCatalogingSearchEngine extends ModCatalogingSearchEngine {
  /**
   * Builds a new Search engine instance with the given data.
   *
   * @param mainLibraryId           the main library identifier.
   * @param databasePreferenceOrder the database preference order.
   * @param service                 the {@link StorageService} instance.
   */
  public LightweightVerticalModCatalogingSearchEngine(final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
    super(mainLibraryId, databasePreferenceOrder, service);
  }

  @Override
  public Record newRecord() {
    return new LightweightVerticalRecord();
  }

  @Override
  public void injectDocCount(SearchResponse searchResponse, final StorageService storageService) throws ModCatalogingException {
    // TODO Auto-generated method stub

  }
}
