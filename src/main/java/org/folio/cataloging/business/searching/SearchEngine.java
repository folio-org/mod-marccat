package org.folio.cataloging.business.searching;

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
	ResultSet expertSearch(String cclQuery, Locale locale, int searchingView) throws ModCatalogingException;
	ResultSet simpleSearch(String query, String use, Locale locale, int searchingView) throws ModCatalogingException;

	ResultSet advancedSearch(List<String> termList,
							 List<String> relationList,
							 List<String> useList,
							 List<String> operatorList,
							 Locale locale, int searchingView) throws ModCatalogingException, SocketTimeoutException;

    ResultSet fetchRecords(ResultSet rs, String elementSetName, int firstRecord, int lastRecord);

	ResultSet sort(
            ResultSet rs,
            String[] attributes,
            String[] directions) throws ModCatalogingException;
}
