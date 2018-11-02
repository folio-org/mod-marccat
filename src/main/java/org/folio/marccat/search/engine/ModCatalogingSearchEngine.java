package org.folio.cataloging.search.engine;

import org.folio.cataloging.Global;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.CatalogItem;
import org.folio.cataloging.exception.ModCatalogingException;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.search.SearchResponse;
import org.folio.cataloging.search.domain.Record;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.IntStream.rangeClosed;

/**
 * Supertype layer for all ModCataloging Search Engine implementations.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public abstract class ModCatalogingSearchEngine implements SearchEngine {
  private final static Log LOGGER = new Log(ModCatalogingSearchEngine.class);
  private final static SearchResponse EMPTY_RESULTSET = new SearchResponse(Integer.MIN_VALUE, Collections.emptyList()) {
    @Override
    public OptionalInt getRecordIdentifier(final int index) {
      return OptionalInt.empty();
    }
  };
  private static final String[] RELATIONSHIP_TABLE = new String[]{"dummy", "<", "<=", "=", ">", ">="};
  private static final Map <Locale, String[]> OPERATORS = new HashMap <>();
  private static Map <Locale, String> DEFAULT_SEARCH_INDEX = new Hashtable <>();

  static {
    DEFAULT_SEARCH_INDEX.put(Locale.ENGLISH, "AW");
  }

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
  public SearchResponse expertSearch(final String cclQuery, final Locale locale, final int searchingView) throws ModCatalogingException {
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
  public SearchResponse simpleSearch(final String query, final String use, final Locale locale, final int searchingView) throws ModCatalogingException {
    return expertSearch(buildCclQuery(query, use, locale), locale, searchingView);
  }

  @Override
  public SearchResponse advancedSearch(final List <String> termList,
                                       final List <String> relationList,
                                       final List <String> useList,
                                       final List <Integer> operatorList,
                                       final Locale locale,
                                       final int searchingView) throws ModCatalogingException {
    return expertSearch(
      buildCclQuery(termList, relationList, useList, operatorList, locale),
      locale,
      searchingView);
  }

  @Override
  public SearchResponse sort(final SearchResponse rs, final String[] attributes, final String[] directions) throws ModCatalogingException {
    return storageService.sortResults(rs, attributes, directions);
  }

  private String buildCclQuery(
    final List <String> termList,
    final List <String> relationList,
    final List <String> useList,
    final List <Integer> operatorList,
    final Locale locale) {
    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < useList.size(); i++) {
      if (i > 0) {
        buffer
          .append(" ")
          .append(getLocalisedOperator(operatorList.get(i), locale))
          .append(" ");
      }
      buffer
        .append(useList.get(i)).append(" ")
        .append(RELATIONSHIP_TABLE[Integer.parseInt(relationList.get(i))])
        .append(" ").append(termList.get(i)).append(" ");
    }

    return buffer.toString();
  }

  /**
   * Builds a CCL query from the given data.
   *
   * @param query  the input query.
   * @param useIn  the index.
   * @param locale the current locale.
   * @return the CCL query.
   */
  private String buildCclQuery(final String query, final String useIn, final Locale locale) {
    final StringBuilder buffer = new StringBuilder();
    final String use = (useIn == null || useIn.trim().isEmpty()) ? getDefaultSearchIndex(locale) : useIn;

    buffer.append(use).append(" = ");
    if (query.trim().matches("\".*\"")) {
      buffer.append(query);
    } else {
      final String[] words = query.trim().split(" ");
      for (int i = 0; i < words.length - 1; i++) {
        buffer.append(
          words[i]
            + " "
            + getLocalisedOperator(1, locale)
            + " "
            + use
            + " = ");
      }
      buffer.append(words[words.length - 1]);
    }
    return buffer.toString();
  }

  /**
   * Returns the localized version of the boolean operator associated with the given index.
   * The index is the operator offset within the localized array. The array contains, at time of writing:
   *
   * <li>
   * <ul>0: empty string</ul>
   * <ul>1: AND</ul>
   * <ul>2: OR</ul>
   * <ul>3: NOT</ul>
   * <ul>4: NEAR</ul>
   * <ul>5: WITH</ul>
   * </li>
   *
   * @param index  the operator index within the i18n bundle.
   * @param locale the current locale.
   * @return the localized version of the boolean operator associated with the given index.
   */
  private String getLocalisedOperator(final int index, final Locale locale) {
    final String[] results = OPERATORS.computeIfAbsent(locale, k -> {
      final ResourceBundle bundle = ResourceBundle.getBundle("/advancedSearch", locale);
      return new String[]{
        "",
        bundle.getString("and"),
        bundle.getString("or"),
        bundle.getString("not"),
        bundle.getString("near"),
        bundle.getString("with")};
    });
    return results[index];
  }

  /**
   * Returns the default index associated with the given locale.
   *
   * @param locale the current locale.
   * @return the default index associated with the input locale.
   */
  private String getDefaultSearchIndex(final Locale locale) {
    return DEFAULT_SEARCH_INDEX.getOrDefault(locale, "AW");
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
}
