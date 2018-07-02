package org.folio.cataloging.search;

import org.folio.cataloging.Global;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.librivision.Record;
import org.folio.cataloging.business.librivision.XmlRecord;
import org.folio.cataloging.business.searching.SearchEngine;
import org.folio.cataloging.exception.ModCatalogingException;
import org.folio.cataloging.integration.StorageService;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.SystemInternalFailureException;

import java.util.*;

import static java.util.Optional.ofNullable;

/**
 * ModCataloging Search Engine.
 * 
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class ModCatalogingSearchEngine implements SearchEngine {
	private final static Log LOGGER = new Log(ModCatalogingSearchEngine.class);
	private final static SearchResponse EMPTY_RESULTSET = new SearchResponse(Integer.MIN_VALUE, Collections.emptyList()) {
		@Override
		public OptionalInt getRecordIdentifier(final int index) {
			return OptionalInt.empty();
		}
	};

	// TODO: does it make sense to have this configurable?
	private static Map<Locale, String> DEFAULT_SEARCH_INDEX = new Hashtable<>();
	static
	{
		DEFAULT_SEARCH_INDEX.put(Locale.ENGLISH, "AW");
	}

	private static String[] RELATIONSHIP_TABLE = new String[] { "dummy", "<", "<=", "=", ">", ">=" };

	private static Map operators = new Hashtable();

	private final int mainLibraryId;
	private final int databasePreferenceOrder;
	private final StorageService storageService;

    /**
     * Builds a new Search engine instance with the given data.
     *
     * @param mainLibraryId the main library identifier.
     * @param databasePreferenceOrder the database preference order.
     * @param service the {@link StorageService} instance.
     */
	public ModCatalogingSearchEngine(final int mainLibraryId, final int databasePreferenceOrder, final StorageService service) {
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
	public SearchResponse fetchRecords(final SearchResponse ars, final String elementSetName, final int firstRecord, final int lastRecord) {
		int itemNumber;
		int searchingView = ars.getSearchingView();
		try {
			for (int i = firstRecord; i <= lastRecord; i++) {
			    final int pos = i -1;
				itemNumber = ars.getIdSet()[pos];
				/*
				 * pm 2011 The test below is added for the case we are fetching
				 * the variants of a bib item. The view to be fetched is pre-set
				 * in each result set record.
				 */
				if (ars.getRecord()[pos] != null	&& ars.getRecord()[pos].getRecordView() > 0) {
					searchingView = ars.getRecord()[pos].getRecordView();
				} else if (ars.getSearchingView() == View.ANY) {
					searchingView = storageService.getPreferredView(itemNumber, databasePreferenceOrder);
				}

                String recordData = null;
				try {
                    recordData = storageService.getRecordData(itemNumber, searchingView);
                } catch (final RecordNotFoundException exception) {
                    try {
                        final CatalogItem item = storageService.getCatalogItemByKey(itemNumber, searchingView);
                        storageService.updateFullRecordCacheTable(item, searchingView);
                        recordData = storageService.getRecordData(itemNumber, searchingView);
                    } catch (final Exception fallback) {
                        recordData = Global.EMPTY_STRING;
                    }
                }

                final Record record =
                        ofNullable(ars.getRecord()[pos])
                            .orElseGet(() -> ars.setRecord(pos, new XmlRecord()));

				((XmlRecord) record).setContent(elementSetName, recordData);
				record.setRecordView(searchingView);
			}
			return ars;
		} catch (final Exception exception) {
			LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			throw new RuntimeException(exception);
		}
	}

	@Override
	public SearchResponse simpleSearch(final String query, final String use, final Locale locale, final int searchingView) throws ModCatalogingException {
		return expertSearch(buildCclQuery(query, use, locale), locale, searchingView);
	}

	@Override
	public SearchResponse advancedSearch(final List<String> termList,
                                         final List<String> relationList,
                                         final List<String> useList,
                                         final List<String> operatorList,
                                         final Locale locale,
                                         final int searchingView) throws ModCatalogingException {
		return expertSearch(
				buildCclQuery(termList, relationList, useList, operatorList, locale),
				locale,
				searchingView);
	}

	public SearchResponse sort(final SearchResponse rs, final String[] attributes, final String[] directions) throws ModCatalogingException {
		return storageService.sortResults(rs, attributes, directions);
	}

	private String buildCclQuery(
			final List<String> termList,
			final List<String> relationList,
			final List<String> useList,
			final List<String> operatorList,
			final Locale locale) {
		final StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < useList.size(); i++) {
			if (i > 0) {
				buffer
                    .append(" ")
                    .append(getLocalisedOperator(operatorList.get(i),locale))
                    .append(" ");
			}
			buffer
				.append(useList.get(i)).append(" ")
				.append(RELATIONSHIP_TABLE[Integer.parseInt(relationList.get(i))])
				.append(" ").append(termList.get(i)).append(" ");
		}

		return buffer.toString();
	}

	private String buildCclQuery(final String query, final String useIn, final Locale locale) {
		final StringBuilder buffer = new StringBuilder();
		final String use =
				(useIn == null || useIn.trim().isEmpty())
						? getDefaultSearchIndex(locale)
						: useIn;

		buffer.append(use).append(" = ");
		if (query.trim().matches("\".*\"")) {
			buffer.append(query);
		} else {
			final String[] words = query.trim().split(" ");
			for (int i = 0; i < words.length - 1; i++) {
				buffer.append(
						words[i]
								+ " "
								+ getLocalisedOperator("1", locale)
								+ " "
								+ use
								+ " = ");
			}
			buffer.append(words[words.length - 1]);
		}
		return buffer.toString();
	}

	private String getLocalisedOperator(String code, Locale locale) {
		String[] results = (String[]) operators.get(locale);
		if (results == null) {
			results = new String[6];
			results[0] = "";
			ResourceBundle bundle =	ResourceBundle.getBundle("resources/searching/advancedSearch", locale);
			results[1] = bundle.getString("and");
			results[2] = bundle.getString("or");
			results[3] = bundle.getString("not");
			results[4] = bundle.getString("near");
			results[5] = bundle.getString("with");
			operators.put(locale, results);
		}
		return results[Integer.parseInt(code)];
	}

    private String getDefaultSearchIndex(final Locale locale) {
        return DEFAULT_SEARCH_INDEX.getOrDefault(locale, "AW");
    }
}
