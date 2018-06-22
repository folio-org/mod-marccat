package org.folio.cataloging.search;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.cataloging.business.amicusSearchEngine.AmicusResultSet;
import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.librivision.Record;
import org.folio.cataloging.business.librivision.XmlRecord;
import org.folio.cataloging.business.searching.ResultSet;
import org.folio.cataloging.business.searching.SearchEngine;
import org.folio.cataloging.dao.*;
import org.folio.cataloging.dao.persistence.FULL_CACHE;
import org.folio.cataloging.exception.ModCatalogingException;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.cataloging.resources.SystemInternalFailureException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * 2018 ModCataloging Search Engine.
 * 
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class ModCatalogingSearchEngine implements SearchEngine {
	private final static Log LOGGER = new Log(ModCatalogingSearchEngine.class);
	private final static ResultSet EMPTY_RESULTSET = new ResultSet() {
		@Override
		public Integer getAmicusNumber(final int index) {
			return null;
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

	private final UserProfile userProfile;
	private final String librarySymbol;
	private final Session session;

    /**
     * Builds a new Search engine instance with the given data.
     *
     * @param userProfile the user profile.
     * @param session the hibernate session.
     */
	public ModCatalogingSearchEngine(final UserProfile userProfile, final Session session) {
		this.session = session;
		this.userProfile = userProfile;
		try {
			this.librarySymbol = new DAOLibrary().load(userProfile.getMainLibrary()).getLibrarySymbolCode();
		} catch (final DataAccessException exception) {
			LOGGER.error("No library symbol found for org " + userProfile.getMainLibrary());
			throw new SystemInternalFailureException(exception);
		}
	}

	@Override
	public ResultSet expertSearch(final String cclQuery, final Locale locale, final int searchingView) throws ModCatalogingException {
		final Parser parser = new Parser(locale, userProfile, searchingView);
		try (final Connection connection = session.connection();
			 final Statement sql = stmt(connection);
			 final java.sql.ResultSet rs = executeQuery(sql, parser.parse(cclQuery))) {

			final ArrayList<Integer> results = new ArrayList<>();
			while (rs.next()) {
				results.add(rs.getInt(1));
			}

			if (results.size() == 0) {
				return EMPTY_RESULTSET;
			}

			return new AmicusResultSet(
					this,
					searchingView,
					cclQuery,
					results.stream().mapToInt(Integer::intValue).toArray());
			
		} catch (final HibernateException | SQLException exception) {
			throw new DataAccessException(exception);
		}
	}

	String getDefaultSearchIndex(Locale locale) {
		return DEFAULT_SEARCH_INDEX.getOrDefault(locale, "AW");
	}

	@Override
	public ResultSet fetchRecords(final ResultSet rs, final String elementSetName, final int firstRecord, final int lastRecord) {
		AmicusResultSet ars = (AmicusResultSet) rs;
		DAOCache daoCache = new DAOCache();
		int itemNumber;
		int searchingView = ars.getSearchingView();
		try {
			for (int i = firstRecord; i <= lastRecord; i++) {
				itemNumber = ars.getAmicusNumbers()[i - 1];
				/*
				 * pm 2011 The test below is added for the case we are fetching
				 * the variants of a bib item. The view to be fetched is pre-set
				 * in each result set record.
				 */
				if (ars.getRecord()[i - 1] != null	&& ars.getRecord()[i - 1].getRecordView() > 0) {
					searchingView = ars.getRecord()[i - 1].getRecordView();
				} else if (ars.getSearchingView() == View.ANY) {
					int preferredView = daoCache.getPreferredView(itemNumber, userProfile.getDatabasePreferenceOrder());
					searchingView = preferredView;
				}

				FULL_CACHE cache = null;
				try {
					cache = new DAOFullCache().load(itemNumber, searchingView);
				} catch (RecordNotFoundException e) {
					try {
						final Catalog theCatalog = Catalog.getInstanceByView(searchingView);
						final CatalogDAO theDao = theCatalog.getCatalogDao();
						final CatalogItem theItem = theDao.getCatalogItemByKey(new Object[] { itemNumber, searchingView });

						theDao.updateFullRecordCacheTable(theItem);
						cache = new DAOFullCache().load(itemNumber,	searchingView);
					} catch (Exception e2) {
						cache = new FULL_CACHE(itemNumber, searchingView);
						cache.setRecordData("");
					}
				}

				Record aRecord = ars.getRecord()[i - 1];
				if (aRecord == null) {
					aRecord = new XmlRecord();
					ars.setRecord(i - 1, aRecord);
				}
				((XmlRecord) aRecord).setContent(elementSetName, cache.getRecordData());
				aRecord.setRecordView(searchingView);
			}
			return rs;
		} catch (final Exception exception) {
			LOGGER.error(MessageCatalog._00010_DATA_ACCESS_FAILURE, exception);
			throw new RuntimeException(exception);
		}
	}

	@Override
	public ResultSet simpleSearch(final String query, final String use, final Locale locale, final int searchingView) throws ModCatalogingException {
		return expertSearch(buildCclQuery(query, use, locale), locale, searchingView);
	}

	@Override
	public ResultSet advancedSearch(final List<String> termList,
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

	public ResultSet sort(final ResultSet rs, final String[] attributes, final String[] directions) throws ModCatalogingException {
		new DAOSortResultSets().sort((AmicusResultSet) rs, attributes, directions);
		rs.clearRecords();
		return rs;
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
				buffer.append(" " + getLocalisedOperator(operatorList.get(i),locale)	+ " ");
			}
			buffer
				.append(useList.get(i) + " ")
				.append(RELATIONSHIP_TABLE[Integer.parseInt(relationList.get(i))])
				.append(" " + termList.get(i) + " ");
		}

		return buffer.toString();
	}

	private String buildCclQuery(final String query, final String useIn, final Locale locale) {
		final StringBuilder buffer = new StringBuilder();
		final String use =
				(useIn == null || useIn.trim().isEmpty())
						? getDefaultSearchIndex(locale)
						: useIn;

		buffer.append(use + " = ");
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

    private Statement stmt(final Connection connection) {
        try {
            return connection.createStatement();
        } catch (final Exception exception) {
            throw new ModCatalogingException(exception);
        }
    }

    private java.sql.ResultSet executeQuery(final Statement stmt, final String query) {
        try {
            return stmt.executeQuery(query);
        } catch (final Exception exception) {
            throw new ModCatalogingException(exception);
        }
    }
}
