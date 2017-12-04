package org.folio.cataloging.bean.searching;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.folio.cataloging.business.codetable.ValueLabelElement;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.exception.LibrisuiteException;
import org.folio.cataloging.dao.DAORestrictions;
import org.folio.cataloging.dao.DAOSearchIndex;
import org.folio.cataloging.business.searching.NoResultsFoundException;
import org.folio.cataloging.business.searching.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.folio.cataloging.business.controller.SessionUtils;
import org.folio.cataloging.business.controller.UserProfile;

/**
 * Manages presentation output for the browse frame.
 * 
 * @author carment
 * @since 1.0
 */
public class AdvancedSearchBean extends SearchBean {

	private static final Log logger =
		LogFactory.getLog(AdvancedSearchBean.class);

	private List termList = new ArrayList();

	private List relationList = new ArrayList();

	private List useList = new ArrayList();

	private List operatorList = new ArrayList();

	private List indexPrimaryList = new ArrayList();

	private List indexSecondaryList = new ArrayList();

	private List indexPrimarySubList = new ArrayList();

	private List indexSecondarySubList = new ArrayList();

	private List searchIndexList = new ArrayList();

	private List indexTypeList = new ArrayList();
	
	private List<RestrictionBean> restrictionsList = new ArrayList<RestrictionBean>();

	private List restrictionsLabelList = new ArrayList<RestrictionBean>();
	
	private String codeTable = new String();

	private boolean showCodeTable = false;
	
	private UserProfile userProfile;
	
	private int termListIndex;
	
	private String restrictionValue;
	private String codeValue;
//	20120229 inizio
	private String wildCards;
	
	public String getWildCards() {
		return wildCards;
	}

	public void setWildCards(String wildCards) {
		this.wildCards = wildCards;
	}	
//	20120229 fine
	
	/*modifica barbara 18/04/2007 per pagina ricerca*/
	private static HttpServletRequest requestBean=null;
	
	/*modifica barbara 17/04/2007 per pagina ricerca*/
	private char indexT ;
	public char getIndexT() {
		return indexT;
	}

	public void setIndexT(char indexT) {
		this.indexT = indexT;
	}

	/*modifica barbara 17/04/2007 per pagina ricerca*/
	private int ind;
	public int getInd() {
		return ind;
	}

	public void setInd(int ind) {
		this.ind = ind;
	}

	/*modifica barbara 17/04/2007 per pagina ricerca*/
	private String indValue;
	public String getIndValue() {
		return indValue;
	}

	public void setIndValue(String indValue) {
		this.indValue = indValue;
	}

	/*modifica barbara 19/04/2007 per pagina ricerca*/
	private List codeTableList = new ArrayList();
	public List getCodeTableList() {
		return codeTableList;
	}

	public void setCodeTableList(List codeTableList) {
		this.codeTableList = codeTableList;
	}

	/*modifica barbara 20/04/2007 per pagina ricerca*/
	private List undoList = new ArrayList();		
	public List getUndoList() {
		return undoList;
	}

	public void setUndoList(List undoList) {
		this.undoList = undoList;
	}
	/*modifica barbara 20/04/2007 per pagina ricerca*/
	private int nextCommand = 0;
	public int getNextCommand() {
		return nextCommand;
	}

	public void setNextCommand(int nextCommand) {
		this.nextCommand = nextCommand;
	}
	
	public AdvancedSearchBean() {
		super.setAdvancedSearch(true);
	}

	public static SearchBean getInstance(HttpServletRequest request) {
		AdvancedSearchBean bean =
			(AdvancedSearchBean) AdvancedSearchBean.getSessionAttribute(
				request,
				AdvancedSearchBean.class);
		/*modifica barbara 18/04/2007 per pagina ricerca*/
		requestBean=request;
		if (bean == null) {
			bean = new AdvancedSearchBean();
			bean.setSessionAttribute(request, AdvancedSearchBean.class);
			bean.userProfile = SessionUtils.getUserProfile(request);

			DAOSearchIndex dao = new DAOSearchIndex();

			/*try {
				bean.setIndexPrimaryList(
					dao.getIndexCategories(request.getSession(false),
						SessionUtils.getCurrentLocale(
							request.getSession(false)),
						"P"));
				bean.setIndexSecondaryList(
					dao.getIndexCategories(
							request.getSession(false),SessionUtils.getCurrentLocale(
							request.getSession(false)),
						"S"));


				bean.setIndexT('P');
				
			} catch (DataAccessException e) {
				throw new RuntimeException("failed to load indexes", e);
			}*/
		}
		bean.setSessionAttribute(request, SearchBean.class);
		SearchBean.getInstance(request);
		return bean;
	}

	/**
	 * Getter for operatorList
	 * 
	 * @return operatorList
	 * @since 1.0
	 */
	public List getOperatorList() {
		return operatorList;
	}

	/**
	 * Getter for relationList
	 * 
	 * @return relationList
	 * @since 1.0
	 */
	public List getRelationList() {
		return relationList;
	}

	/**
	 * Getter for termList
	 * 
	 * @return termList
	 * @since 1.0
	 */
	public List getTermList() {
		return termList;
	}

	/**
	 * Getter for useList
	 * 
	 * @return useList
	 * @since 1.0
	 */
	public List getUseList() {
		return useList;
	}

	/**
	 * Setter for operatorList
	 * 
	 * @param list operatorList
	 * @since 1.0
	 */
	public void setOperatorList(List list) {
		operatorList = list;
	}
/**
 * Determines if conditions are correct to display "New Record" button on
 * AdvancedSearch page
 * @return
 */
	public boolean isShowNewRecordButton() {
		if (getSearchingView() == View.AUTHORITY) {
			if (getUserProfile().getDefaultAuthorityModel() != null) {
				return true;
			}
		}
		else {
			if (getUserProfile().getDefaultBibliographicModel() != null) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Setter for relationList
	 * 
	 * @param list relationList
	 * @since 1.0
	 */
	public void setRelationList(List list) {
		relationList = list;
	}

	/**
	 * Setter for termList
	 * 
	 * @param list termList
	 * @since 1.0
	 */
	public void setTermList(List list) {
		termList = list;
	}

	/**
	 * Setter for useList
	 * 
	 * @param list useList
	 * @since 1.0
	 */
	public void setUseList(List list) {
		useList = list;
	}

	public void setOperatorList(int index, String string) {
		operatorList.set(index, string);
	}

	public void setRelationList(int index, String string) {
		relationList.set(index, string);
	}

	public void setTermList(int index, String string) {
		termList.set(index, string);
	}

	public void setUseList(int index, String string) {
		useList.set(index, string);
	}

	public void setSearchIndexList(int index, String string) {
		searchIndexList.set(index, string);
	}

	public void initList(Locale locale) {

		termList.clear();
		termList.add(new String());
		useList.clear();
		useList.add(new String());
		relationList.clear();
		relationList.add(new String("3"));
		operatorList.clear();
		operatorList.add(new String("1"));
		operatorList.add(new String("0"));
		searchIndexList.clear();
		searchIndexList.add(
			new String(
				ResourceBundle
					.getBundle("resources.searching.advancedSearch", locale)
					.getString("selectIndex")));
		indexTypeList.add(new String("P"));
	}

	public void addSearchTerm(Locale locale) {

		termList.add(new String());
		useList.add(new String());
		relationList.add(new String("3"));
		operatorList.add(new String("0"));
		searchIndexList.add(
			new String(
				ResourceBundle
					.getBundle("resources.searching.advancedSearch", locale)
					.getString("selectIndex")));
		indexTypeList.add(new String("P"));
	}

	public void removeSearchTerm(int index) {
		termList.remove(index);
		useList.remove(index);
		relationList.remove(index);
		operatorList.remove(index);
	}

	/**
	 * Getter for indexList
	 * 
	 * @return indexList
	 * @since 1.0
	 */
	public List getIndexPrimaryList() {
		return indexPrimaryList;
	}

	public List getIndexSecondaryList() {
		return indexSecondaryList;
	}

	/**
	 * Setter for indexList
	 * 
	 * @param list indexList
	 * @since 1.0
	 */
	public void setIndexPrimaryList(List list) {
		indexPrimaryList = list;
	}

	public void setIndexSecondaryList(List list) {
		indexSecondaryList = list;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public String getCodeTable() {
		return codeTable;
	}

	/**
	 * 
	 * 
	 * @param string
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setCodeTable(String string) {
		codeTable = string;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public boolean isShowCodeTable() {
		return showCodeTable;
	}

	/**
	 * 
	 * 
	 * @param b
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setShowCodeTable(boolean b) {
		showCodeTable = b;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public int getTermListIndex() {
		return termListIndex;
	}

	/**
	 * 
	 * 
	 * @param i
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setTermListIndex(int i) {
		termListIndex = i;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public List getIndexPrimarySubList() {
		return indexPrimarySubList;
	}

	public List getIndexSecondarySubList() {
		return indexSecondarySubList;
	}

	/**
	 * 
	 * 
	 * @param list
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndexPrimarySubList(List list) {
		indexPrimarySubList = list;
	}

	public void setIndexSecondarySubList(List list) {
		indexSecondarySubList = list;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public List getSearchIndexList() {
		return searchIndexList;
	}

	/**
	 * 
	 * 
	 * @param list
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setSearchIndexList(List list) {
		searchIndexList = list;
	}

	/**
	 * 
	 * 
	 * @return
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public List getIndexTypeList() {
		return indexTypeList;
	}

	/**
	 * 
	 * 
	 * @param list
	 * @exception
	 * @see
	 * @since 1.0
	 */
	public void setIndexTypeList(List list) {
		indexTypeList = list;
	}

	public String calculateQuery(Locale locale) {
		String query = new String("");

		for (int i = 0; i < termList.size(); i++) {
			if (i > 0) {
				query =
					query
						+ new String(
							ResourceBundle
								.getBundle(
									"resources.searching.advancedSearch",
									locale)
								.getString(
									"operator"
										+ operatorList.get(i).toString()))
						+ " ";
			}

			query = query + useList.get(i).toString() + " ";

			query =
				query
					+ new String(
						ResourceBundle
							.getBundle(
								"resources.searching.advancedSearch",
								locale)
							.getString(
								"relation" + relationList.get(i).toString()))
					+ " ";

			query = query + termList.get(i).toString() + " ";

		}

		return query;
	}

	public ResultSet advancedSearch(
		List termList,
		List relationList,
		List useList,
		List operatorList,
		Locale locale,
		int searchingView)
		throws NoResultsFoundException, LibrisuiteException,SocketTimeoutException {
		logger.debug("Start search");
		ResultSet resultSet;
		resultSet =
			getSearchEngine().advancedSearch(
				termList,
				relationList,
				useList,
				operatorList,
				locale,
				getSearchingView());
		fetchAndArchiveSearchResult(resultSet);
		setQuery(calculateQuery(locale));
		return resultSet;
	}

	/* (non-Javadoc)
	 * @see SearchBean#getSearchType()
	 */
	public String getSearchType() {
		return "advancedSearch";
	}

	/*modifica barbara 18/04/2007 per pagina ricerca*/
	public List getIndexSubList(char indT, int ind) {
		DAOSearchIndex dao = new DAOSearchIndex();

			try {
				return
					dao.getSubIndex(
						SessionUtils.getCurrentLocale(
							requestBean.getSession(false)),
						indT, ind);
			} catch (DataAccessException e) {
				throw new RuntimeException("failed to load indexes", e);
			}
		}
	/*modifica barbara 20/04/2007 per pagina ricerca*/
	public ResultSet expertSearch(String cclQuery, Locale locale, int searchingView)
	throws NoResultsFoundException, LibrisuiteException {
	logger.debug("Start search");
	ResultSet resultSet;
	resultSet =
		getSearchEngine().expertSearch(cclQuery, locale, searchingView);
	fetchAndArchiveSearchResult(resultSet);
	setQuery(cclQuery);
	return resultSet;
	}
	
	/*modifica barbara 20/04/2007 per pagina ricerca*/
	public ResultSet expertFrbrSearch(String cclQuery, Locale locale, int searchingView)
	throws NoResultsFoundException, LibrisuiteException {
	logger.debug("Start search");
	ResultSet resultSet;
	resultSet =
		getSearchEngine().expertSearch(cclQuery, locale, searchingView);
	fetchFrbrAndArchiveSearchResult(resultSet);
	setQuery(cclQuery);
	return resultSet;
	}
	
	
	/*modifica barbara 20/04/2007 per pagina ricerca*/
	public ResultSet expertSearch(String cclQuery, String complexQuery,Locale locale)
	throws NoResultsFoundException, LibrisuiteException {
	logger.debug("Start search");
	ResultSet resultSet;
	//pm 2011 -- change from parameter to locally maintained searchingView
	resultSet =
		getSearchEngine().expertSearch(cclQuery, locale, getSearchingView());
	resultSet.setComplexQuery(complexQuery);
	fetchAndArchiveSearchResult(resultSet);
	setQuery(cclQuery);
	return resultSet;
	}
	
	public void clearLists() {
		undoList.clear();
		undoList.add("");
		nextCommand = 1;
		setQuery("");
	}

	public List<RestrictionBean> getRestrictionsList() {
		return restrictionsList;
	}

	public void setRestrictionsList(List<RestrictionBean> restrictionsList) {
		this.restrictionsList = restrictionsList;
	}

	public List getRestrictionsLabelList() {
		return restrictionsLabelList;
	}

	public void setRestrictionsLabelList(List restrictionsLabelList) {
		this.restrictionsLabelList = restrictionsLabelList;
	}
	
	public void createRestrictions(String username)
	{
		try 
		{	
					DAORestrictions dao = new DAORestrictions();
					List<RestrictionBean> list = dao.getRestrictions(username);
		
					List<ValueLabelElement> vl = new ArrayList<ValueLabelElement>();
		
					for(RestrictionBean bean: list)
					{
						vl.add(new ValueLabelElement(bean.getValue(),bean.getName()));
					}
		
					setRestrictionsList(list);
					setRestrictionsLabelList(vl);
		} catch (DataAccessException e) {
			throw new RuntimeException("failed to load restrictions", e);
		}
	}

	public String getRestrictionValue() {
		return restrictionValue;
	}

	public void setRestrictionValue(String restrictionValue) {
		this.restrictionValue = restrictionValue;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	private UserProfile getUserProfile() {
		return userProfile;
	}

	private void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}	

}