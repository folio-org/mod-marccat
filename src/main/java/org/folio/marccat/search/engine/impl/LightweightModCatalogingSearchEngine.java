package org.folio.marccat.search.engine.impl;

import net.sf.hibernate.HibernateException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.CountDocument;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.search.domain.LightweightJsonRecord;
import org.folio.marccat.search.domain.Record;
import org.folio.marccat.search.engine.ModCatalogingSearchEngine;

import java.util.Arrays;

/**
 * ModMarccat Search Engine.
 *
 * @author cchiama
 * @since 1.0
 */
public class LightweightModCatalogingSearchEngine extends ModCatalogingSearchEngine {
  /**
   * Builds a new Search engine instance with the given data.
   *
   * @param mainLibraryId           the main library identifier.
   * @param databasePreferenceOrder the database preference order.
   * @param service                 the {@link StorageService} instance.
   */
  public LightweightModCatalogingSearchEngine(final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
    super(mainLibraryId, databasePreferenceOrder, service);
  }

  @Override
  public Record newRecord() {
    return new LightweightJsonRecord();
  }

  @Override
  public void injectDocCount(SearchResponse searchResponse, final StorageService storageService) throws ModMarccatException {
    final int view = 1;
    //retrieve records id
    if (searchResponse != null) {
      Arrays.stream(searchResponse.getRecord()).forEach(singleRecord -> {
        int an = ((LightweightJsonRecord) singleRecord).getData().get("fields").get(0).get("001").asInt();
        try {
          CountDocument countDocument = storageService.getCountDocumentByAutNumber(an, view);
          singleRecord.setCountDoc(countDocument.getCountDocuments());
          singleRecord.setQueryForAssociatedDoc(countDocument.getQuery());
        } catch (HibernateException e) {
          e.printStackTrace();
        }

      });
    }

  }


}
