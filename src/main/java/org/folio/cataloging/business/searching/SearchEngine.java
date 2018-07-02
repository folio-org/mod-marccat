package org.folio.cataloging.business.searching;

import org.folio.cataloging.search.SearchResponse;
import org.folio.cataloging.exception.ModCatalogingException;

import java.net.SocketTimeoutException;
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
	SearchResponse expertSearch(String cclQuery, Locale locale, int searchingView) throws ModCatalogingException;
	SearchResponse simpleSearch(String query, String use, Locale locale, int searchingView) throws ModCatalogingException;

	SearchResponse advancedSearch(List<String> termList,
                                  List<String> relationList,
                                  List<String> useList,
                                  List<String> operatorList,
                                  Locale locale, int searchingView) throws ModCatalogingException, SocketTimeoutException;

    SearchResponse fetchRecords(SearchResponse rs, String elementSetName, int firstRecord, int lastRecord);

	SearchResponse sort(
            SearchResponse rs,
            String[] attributes,
            String[] directions) throws ModCatalogingException;
}
