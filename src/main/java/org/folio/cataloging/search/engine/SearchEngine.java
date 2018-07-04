package org.folio.cataloging.search;

import org.folio.cataloging.search.SearchResponse;
import org.folio.cataloging.exception.ModCatalogingException;

import java.util.List;
import java.util.Locale;

/**
 * Search Engine interface.
 * 
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public interface SearchEngine {
    /**
     * Expert search interface contract.
     *
     * @param cclQuery the input CCL query.
     * @param locale the current locale.
     * @param searchingView the searching view associated with the current query execution.
     * @return a search response (containing only the docids)
     * @throws ModCatalogingException in case of a search subsystem failure.
     */
	SearchResponse expertSearch(String cclQuery, Locale locale, int searchingView) throws ModCatalogingException;

    /**
     * Simple search interface contract.
     *
     * @param query the input query.
     * @param locale the current locale.
     * @param searchingView the searching view associated with the current query execution.
     * @return a search response (containing only the docids)
     * @throws ModCatalogingException in case of a search subsystem failure.
     */
	SearchResponse simpleSearch(String query, String use, Locale locale, int searchingView) throws ModCatalogingException;

    /**
     * Advanced search interface contract.
     *
     * @param termList
     * @param relationList
     * @param useList
     * @param operatorList
     * @param locale the current locale.
     * @param searchingView the searching view associated with the current query execution.
     * @return a search response (containing only the docids)
     * @throws ModCatalogingException in case of a search subsystem failure.
     */
	SearchResponse advancedSearch(List<String> termList,
                                  List<String> relationList,
                                  List<String> useList,
                                  List<Integer> operatorList,
                                  Locale locale, int searchingView) throws ModCatalogingException;

    /**
     * Fetches the records on a preexistent search response.
     * This is the second phase of a search, where collected docids are fetched in order to retrieve the
     * actual record data.
     *
     * @param searchResponse the output of a search method.
     * @param elementSetName the elemement set name indicates the perspective (Brief or Full) of fetched records.
     * @param firstRecord the start offset (1-based), inclusive.
     * @param lastRecord the end offset, inclusive.
     * @return a search response where records have been collected with the whole record data.
     * @throws ModCatalogingException in case of a search subsystem failure.
     */
    SearchResponse fetchRecords(SearchResponse searchResponse, String elementSetName, int firstRecord, int lastRecord) throws ModCatalogingException;

    /**
     * Sorts a given resultset using the input criteria.
     * Note that this object is supposed to operate before the first (search) and the second phase (fetch).
     * If, instead a fetched {@link SearchResponse} instance is passed as input, then the collected records will be
     * removed (so you will have to call the fetch method again).
     *
     * @param searchResponse the output of a search method.
     * @param attributes the sort attributes.
     * @param directions the sort order.
     * @return a search response without any collected records and with docids sorted according with the requested criteria.
     * @throws ModCatalogingException in case of a search subsystem failure.
     */
	SearchResponse sort(
            SearchResponse searchResponse,
            String[] attributes,
            String[] directions) throws ModCatalogingException;
}