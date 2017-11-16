/*
 * (c) LibriCore
 * 
 * Created on 17-aug-2004
 * 
 * SearchBean.java
 */
package librisuite.bean.searching;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import librisuite.bean.LibrisuiteBean;
import librisuite.bean.cataloguing.bibliographic.codelist.CodeListsBean;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Defaults;
import librisuite.business.common.View;
import librisuite.business.searching.NoResultsFoundException;
import librisuite.business.searching.ResultSet;
import librisuite.business.searching.SearchEngine;
import librisuite.business.searching.SearchEngineFactory;
import librisuite.hibernate.DB_LIST;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.libricore.librisuite.controller.SessionUtils;

/**
 * @author Wim Crols
 * @version $Revision: 1.6 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public abstract class SearchBean extends LibrisuiteBean {

	private static Log logger = LogFactory.getLog(SearchBean.class);

	private SearchEngine searchEngine =
		SearchEngineFactory.getInstance(
			Defaults.getClazz("searchEngine.class"));

	private boolean simpleSearch = true;

	private boolean advancedSearch = false;

	private boolean expertSearch = false;

	private boolean browseIndexes = false;

	// MIKE: Added mades case
	private int currentSearchingView = -2;

	private SearchHistoryBean searchHistoryBean;

	private ResultSummaryBean resultSummaryBean;

	private String query;

	//pm 2011 - maintain locale locally
	private Locale locale = Locale.getDefault();
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * @return a string identifying the search type (simple, advanced, expert) for
	 * use in actionForward mappings (edit historic queries)
	 * 
	 * @since 1.0
	 */
	abstract public String getSearchType();

	/**
	 * Indicates whether the current search is bibliographic or not (i.e. authority)
	 * @since 1.0
	 */

	public boolean isBibliographic() {
		return currentSearchingView > View.AUTHORITY;
	}
	public boolean isMades() {
		return currentSearchingView < View.AUTHORITY;
	}
	public boolean isAuthoriry() {
		return currentSearchingView == View.AUTHORITY;
	}
	
	public int getSearchingView() {
		return currentSearchingView;
	}
	
	
	/**
	 * pm 2011
	 * @return
	 */
	public boolean isSearchingAny() {
		return currentSearchingView == View.ANY;
	}
	
	public void setSearchingView(int view) {
		logger.debug("setting searchingView to: " + view); //pm 2011
		currentSearchingView = view;
	}
	
	public static SearchBean getInstance(HttpServletRequest httpServletRequest) {
		SearchBean searchBean =
			(SearchBean) getSessionAttribute(httpServletRequest,
				SearchBean.class);
		if (searchBean == null) {
			searchBean = AdvancedSearchBean.getInstance(httpServletRequest);
		}

		searchBean.searchHistoryBean =
			SearchHistoryBean.getInstance(httpServletRequest);
		searchBean.resultSummaryBean =
			ResultSummaryBean.getInstance(httpServletRequest);
		searchBean.searchEngine.setUserProfile(
			SessionUtils.getUserProfile(httpServletRequest));

		//pm 2011
		searchBean.setLocale(SessionUtils.getCurrentLocale(httpServletRequest));
		searchBean.setSearchingView(SessionUtils.getSearchingView(httpServletRequest.getSession(false)));
		
		return searchBean;
	}

	/**
	 * This method clears all tab booleans
	 */
	private void clearTabBooleans() {
		this.simpleSearch = false;
		this.advancedSearch = false;
		this.expertSearch = false;
		this.browseIndexes = false;
	}

	/**
	 * @return Returns the advancedSearch.
	 */
	public boolean isAdvancedSearch() {
		return advancedSearch;
	}

	/**
	 * @param advancedSearch The advancedSearch to set.
	 */
	public void setAdvancedSearch(boolean advancedSearch) {
		if (advancedSearch) {
			clearTabBooleans();
			this.advancedSearch = advancedSearch;
		}
	}

	/**
	 * @return Returns the browseIndexes.
	 */
	public boolean isBrowseIndexes() {
		return browseIndexes;
	}

	/**
	 * @param browseIndexes The browseIndexes to set.
	 */
	public void setBrowseIndexes(boolean browseIndexes) {
		if (browseIndexes) {
			clearTabBooleans();
			this.browseIndexes = browseIndexes;
		}
	}

	/**
	 * @return Returns the expertSearch.
	 */
	public boolean isExpertSearch() {
		return expertSearch;
	}

	/**
	 * @param expertSearch The expertSearch to set.
	 */
	public void setExpertSearch(boolean expertSearch) {
		if (expertSearch) {
			clearTabBooleans();
			this.expertSearch = expertSearch;
		}
	}

	/**
	 * @return Returns the simpleSearch.
	 */
	public boolean isSimpleSearch() {
		return simpleSearch;
	}

	public void fetchAndArchiveSearchResult(ResultSet resultSet) throws NoResultsFoundException 
	{
		if (resultSet == null || resultSet.getSize() <= 0) {
			logger.debug("Search has no results");
			throw new NoResultsFoundException();
		}
		logger.debug("Search has results");

		resultSummaryBean.replaceResultSet(resultSet);

		if (searchHistoryBean != null) {
			searchHistoryBean.addSearchHistoryItem(this, resultSet);

		}
	}
	
	public void fetchFrbrAndArchiveSearchResult(ResultSet resultSet) throws NoResultsFoundException 
	{
		if (resultSet == null || resultSet.getSize() <= 0) {
			logger.debug("Search has no results");
			throw new NoResultsFoundException();
		}
		logger.debug("Search has results");
		
		resultSummaryBean.replaceFrbrResultSet(resultSet);
	}
	

	/**
	 * @param simpleSearch The simpleSearch to set.
	 */
	public void setSimpleSearch(boolean simpleSearch) {
		if (simpleSearch) {
			clearTabBooleans();
			this.simpleSearch = simpleSearch;
		}
	}

	/**
	 * 
	 * @since 1.0
	 */
	public SearchEngine getSearchEngine() {
		return searchEngine;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setQuery(String string) {
		logger.debug("Setting query to '" + string + "'");
		
		query = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public ResultSummaryBean getResultSummaryBean() {
		return resultSummaryBean;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public SearchHistoryBean getSearchHistoryBean() {
		return searchHistoryBean;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setResultSummaryBean(ResultSummaryBean bean) {
		resultSummaryBean = bean;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSearchHistoryBean(SearchHistoryBean bean) {
		searchHistoryBean = bean;
	}
	
	/**
	 * pm 2011
	 * @return
	 */
	public List getSearchViews() {
		
		List searchView=null;
		try {
			searchView = CodeListsBean.getDatabaseViewList().getDAO().getList(DB_LIST.class,getLocale());
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchView;
	}
}