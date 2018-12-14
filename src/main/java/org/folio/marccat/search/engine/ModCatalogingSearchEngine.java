package org.folio.marccat.search.engine;

import org.folio.marccat.business.common.View;
import org.folio.marccat.config.Global;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.integration.StorageService;
import org.folio.marccat.search.SearchResponse;
import org.folio.marccat.search.domain.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.IntStream.rangeClosed;

/**
 * Supertype layer for all ModMarccat Search Engine implementations.
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public abstract class ModCatalogingSearchEngine implements SearchEngine {

  private final int mainLibraryId;
  private final int databasePreferenceOrder;
  private final StorageService storageService;

  /**
   * Builds a new Search engine instance with the given data.
   *
   * @param mainLibraryId           the main library identifier.
   * @param databasePreferenceOrder the database preference order.
   * @param service                 the {@link StorageService} instance.
   */
  protected ModCatalogingSearchEngine(final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
    this.storageService = service;
    this.mainLibraryId = mainLibraryId;
    this.databasePreferenceOrder = databasePreferenceOrder;
  }

  @Override
  public SearchResponse expertSearch(final String cclQuery, final Locale locale, final int searchingView) {
    return new SearchResponse(
      searchingView,
      cclQuery,
      storageService.executeQuery(
        cclQuery,
        mainLibraryId,
        locale,
        searchingView)
        .stream()
        .mapToInt(Integer::intValue).toArray());
  }

  @Override
  public SearchResponse fetchRecords(final SearchResponse response, final String elementSetName, final int firstRecord, final int lastRecord) {
    final AtomicInteger searchingView = new AtomicInteger(response.getSearchingView());
    response.setRecordSet(
      rangeClosed(firstRecord, lastRecord)
        .map(index -> index - 1)
        .filter(pos -> response.getIdSet().length > pos)
        .mapToObj(pos -> {
          final int itemNumber = response.getIdSet()[pos];
          if (response.getSearchingView() == View.ANY) {
            searchingView.set(storageService.getPreferredView(itemNumber, databasePreferenceOrder));
          }

          final Record record = newRecord();
          record.setContent(elementSetName, recordData(itemNumber, searchingView.get()));
          record.setRecordView(searchingView.get());
          return record;
        }).toArray(Record[]::new));
    response.setFrom(firstRecord);
    response.setTo(Math.min(lastRecord, (firstRecord + response.getPageSize())));

    return response;
  }

  @Override
  public SearchResponse sort(final SearchResponse rs, final String[] attributes, final String[] directions) {
    return storageService.sortResults(rs, attributes, directions);
  }

  /**
   * Returns the record data associated with the given item number.
   *
   * @param itemNumber    the record number.
   * @param searchingView the search view.
   * @return the record data associated with the given item number.
   */
  private String recordData(final int itemNumber, final int searchingView) {
    try {
      return storageService.getRecordData(itemNumber, searchingView);
    } catch (final RecordNotFoundException exception) {
      try {
        final CatalogItem item = storageService.getCatalogItemByKey(itemNumber, searchingView);
        storageService.updateFullRecordCacheTable(item, searchingView);
        return storageService.getRecordData(itemNumber, searchingView);
      } catch (final Exception fallback) {
        return Global.EMPTY_STRING;
      }
    }
  }

  /**
   * Creates a record representation according with the rules of this search engine implementation.
   * The concrete SE implementor must define in this method the kind of record that it will manage.
   *
   * @return a record representation according with the rules of this search engine implementation.
   */
  public abstract Record newRecord();


  /**
   * retrieves searched term in query, filtering operators and indexes
   *
   * @param query
   * @return a list of searched term in query, filtering operators and indexes
   */
  public List<String> getTermsFromCCLQuery(final String query) {
    List<String> result = new ArrayList<>();
    //remove filters term from query
    String cleanedQuery = query.replaceAll("LAN\\s\"([^\"]*)\"", "").replaceAll("MAT\\s\"([^\"]*)\"", "").replaceAll("BIB\\s\"([^\"]*)\"", "");
    Pattern p = Pattern.compile("\"([^\"]*)\"");
    Matcher m = p.matcher(cleanedQuery);
    while (m.find()) {
      result.add(cleanPunctuation(m.group(1)));
    }
    return result;
  }

  /**
   * strips all punctuation from the text to help in compare
   *
   * @param text
   * @return text cleaned
   */
  public String cleanPunctuation(final String text) {
    return (text != null) ? text.replaceAll(",|;|\\.|!", "") : null;
  }
}
