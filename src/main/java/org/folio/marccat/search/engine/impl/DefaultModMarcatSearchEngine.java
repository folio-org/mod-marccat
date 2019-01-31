package org.folio.marccat.search.engine.impl;

import org.folio.marccat.integration.StorageService;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.search.XmlRecord;
import org.folio.marccat.search.domain.Record;
import org.folio.marccat.search.engine.ModMarcatSearchEngine;

import java.util.Locale;

/**
 * ModMarccat Search Engine.
 *
 * @author cchiama
 * @since 1.0
 */
public class DefaultModMarcatSearchEngine extends ModMarcatSearchEngine {
  /**
   * Builds a new Search engine instance with the given data.
   *
   * @param mainLibraryId           the main library identifier.
   * @param databasePreferenceOrder the database preference order.
   * @param service                 the {@link StorageService} instance.
   */
  public DefaultModMarcatSearchEngine(final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
    super(mainLibraryId, databasePreferenceOrder, service);
  }

  @Override
  public Record newRecord() {
    return new XmlRecord();
  }

  @Override
  public void injectDocCount(SearchResponse response, StorageService storageService) {
    /**
     * do nothing
     */
  }

  @Override
  public void injectTagHighlight(SearchResponse response, StorageService storageService, Locale locale) {
    /**
     * do nothing
     */
  }
}
