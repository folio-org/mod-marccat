package org.folio.cataloging.search.engine.impl;

import java.util.Arrays;

import org.folio.cataloging.exception.ModCatalogingException;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.resources.domain.CountDocument;
import org.folio.cataloging.search.SearchResponse;
import org.folio.cataloging.search.domain.LightweightJsonRecord;
import org.folio.cataloging.search.domain.Record;
import org.folio.cataloging.search.engine.ModCatalogingSearchEngine;

import net.sf.hibernate.HibernateException;

/**
 * ModCataloging Search Engine.
 *
 * @author agazzarini
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
    super (mainLibraryId, databasePreferenceOrder, service);
  }

  @Override
  public Record newRecord() {
    return new LightweightJsonRecord( );
  }

  @Override
  public void injectDocCount(SearchResponse searchResponse, final StorageService storageService) throws ModCatalogingException {
	  final int view = 1; 
	//retrieve records id
	  if (searchResponse != null) {
		  Arrays.stream(searchResponse.getRecord()).forEach( singleRecord -> {
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
