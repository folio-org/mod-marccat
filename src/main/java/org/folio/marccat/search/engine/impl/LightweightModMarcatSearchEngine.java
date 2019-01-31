package org.folio.marccat.search.engine.impl;

import com.fasterxml.jackson.databind.JsonNode;
import net.sf.hibernate.HibernateException;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.resources.domain.CountDocument;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.search.domain.LightweightJsonRecord;
import org.folio.marccat.search.domain.Record;
import org.folio.marccat.search.engine.ModMarcatSearchEngine;

import java.util.*;

/**
 * ModMarccat Search Engine.
 *
 * @author cchiama
 * @since 1.0
 */
public class LightweightModMarcatSearchEngine extends ModMarcatSearchEngine {
  private final Log logger = new Log(LightweightModMarcatSearchEngine.class);

  /**
   * Builds a new Search engine instance with the given data.
   *
   * @param mainLibraryId           the main library identifier.
   * @param databasePreferenceOrder the database preference order.
   * @param service                 the {@link StorageService} instance.
   */
  public LightweightModMarcatSearchEngine(final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
    super(mainLibraryId, databasePreferenceOrder, service);
  }

  @Override
  public Record newRecord() {
    return new LightweightJsonRecord();
  }

  /**
   * Inject in searchResponse of authority records counter of associated bibliographic records and query to retrieve them
   *
   * @param searchResponse
   * @throws ModMarccatException
   */
  public void injectDocCount(SearchResponse searchResponse, final StorageService storageService) {
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
          logger.error("", e);
        }

      });
    }
  }

  /**
   * Inject list of tags in which searchEngine found query parameter
   *
   * @param searchResponse
   * @throws ModMarccatException
   */

  public void injectTagHighlight(SearchResponse searchResponse, final StorageService storageService, Locale lang) {
    List<String> queryTerms = getTermsFromCCLQuery(searchResponse.getDisplayQuery());

    Arrays.stream(searchResponse.getRecord()).forEach(singleRecord -> {
      List<String> tagHighlighted = new ArrayList<>();
      try {
        JsonNode fields = ((LightweightJsonRecord) singleRecord).getData().get("fields");
        if (fields.isArray()) {
          fields.forEach(tag -> {
            Iterator<String> iterator = tag.fieldNames();
            while (iterator.hasNext()) {
              String tagName = iterator.next();
              JsonNode tagValueNode = tag.get(tagName);
              if (queryTerms.stream().anyMatch(term -> cleanPunctuation(tagValueNode.toString()).toLowerCase().contains(term.toLowerCase())))
                tagHighlighted.add(tagName);
            }
          });
        }
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
      }
      singleRecord.setTagHighlighted(String.join(", ", tagHighlighted));
    });

  }
}
