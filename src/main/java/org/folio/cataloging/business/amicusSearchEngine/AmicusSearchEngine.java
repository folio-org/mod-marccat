/*
 * (c) LibriCore
 * 
 * Created on Jun 16, 2006
 * 
 * AmicusSearchEngine.java
 */
package org.folio.cataloging.business.amicusSearchEngine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.colgate.Colgate;
import org.folio.cataloging.business.colgate.ConversionOutRequest;
import org.folio.cataloging.business.colgate.ConversionOutResponse;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.business.librivision.MarcRecord;
import org.folio.cataloging.business.librivision.Record;
import org.folio.cataloging.business.searching.NoResultsFoundException;
import org.folio.cataloging.business.searching.ResultSet;
import org.folio.cataloging.business.searching.SearchEngine;
import org.folio.cataloging.dao.DAOCache;
import org.folio.cataloging.dao.DAOLibrary;
import org.folio.cataloging.dao.DAOSortResultSets;
import org.folio.cataloging.exception.ConnectException;
import org.folio.cataloging.exception.ModCatalogingException;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2006/12/14 10:31:07 $
 * @since 1.0
 */
public class AmicusSearchEngine implements SearchEngine {
	private static Map defaultSearchIndex = new Hashtable();
	private static final Log logger =
		LogFactory.getLog(AmicusSearchEngine.class);
	private static final String ELEMENT_SETNAME="F";
	/*
	 * Hashtable operators is keyed on locale and populated just-in-time
	 * with the locale specific boolean operator values (a String[])
	 */
	private static Map operators = new Hashtable();
	private  String qGateHostname =
		Defaults.getString("amicus.searchEngine.hostname");
	private int qGatePort =
		Defaults.getInteger("amicus.searchEngine.qgate.port");

	private static String[] relationsTable =
		new String[] { "dummy", "<", "<=", "=", ">", ">=" };

	/**
	 * 
	 * @since 1.0
	 */
	public String getDefaultSearchIndex(Locale locale) {
		String result = (String) defaultSearchIndex.get(locale);
		if (result == null) {
			ResourceBundle bundle =
				ResourceBundle.getBundle(
					"resources/searching/simpleSearch",
					locale);
			result = bundle.getString("defaultSearchIndex");
			defaultSearchIndex.put(locale, result);
		}
		return result;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public  String getQGateHostname() {
		return qGateHostname;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public  int getQGatePort() {
		return qGatePort;
	}

	private String librarySymbol;
	private Socket socket;
	protected UserProfile userProfile;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public AmicusSearchEngine() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.searching.SearchEngine#advancedSearch(java.util.List, java.util.List, java.util.List, java.util.List, java.util.Locale, int)
	 */
	public ResultSet advancedSearch(
		List termList,
		List relationList,
		List useList,
		List operatorList,
		Locale locale,
		int searchingView)
		throws ModCatalogingException {
		String cclQuery =
			buildCclQuery(
				termList,
				relationList,
				useList,
				operatorList,
				locale);
		return expertSearch(cclQuery, locale, searchingView);
	}

	private String buildCclQuery(
		List termList,
		List relationList,
		List useList,
		List operatorList,
		Locale locale) {

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < useList.size(); i++) {
			if (i > 0) {
				/*
				 * Don't be confused by the positions and values of the boolean operators
				 * in the list (as I was).
				 * Even though the jsp shows for example "or" in position 0 and 
				 * "no operator" in position 1, when it gets to this search, the 0th
				 * stringValue is always "and" and should be ignored.  The other "real"
				 * operators are shifted down 1 position and the "no operator" is dropped
				 * from the list.  So, whereas visually on the page it looks like the 
				 * operator is placed after the term, in this list, the operator is placed
				 * before the term.  I assume that this is the way LibriVision expected
				 * these values so I have not changed it [for now]
				 */
				buf.append(
					" "
						+ getLocalisedOperator(
							(String) operatorList.get(i),
							locale)
						+ " ");
			}
			buf.append(useList.get(i) + " ");
			buf.append(
				relationsTable[Integer.parseInt((String) relationList.get(i))]);
			buf.append(" " + termList.get(i) + " ");

		}
		logger.debug("buf: '" + buf.toString() + "'");
		return buf.toString();
	}

	private String buildCclQuery(String query, String use, Locale locale) {
		StringBuffer buf = new StringBuffer();

		logger.debug("query is '" + query + "'");
		if (use == null || use.equals("")) {
			use = getDefaultSearchIndex(locale);
			logger.debug("Using default index: '" + use + "'");
		}
		buf.append(use + " = ");
		if (query.trim().matches("\".*\"")) {
			buf.append(query);
		} else {
			String[] words = query.trim().split(" ");
			for (int i = 0; i < words.length - 1; i++) {
				buf.append(
					words[i]
						+ " "
						+ getLocalisedOperator("1", locale)
						+ " "
						+ use
						+ " = ");
			}
			buf.append(words[words.length - 1]);
			logger.debug("buf is: '" + buf.toString() + "'");
		}
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.searching.SearchEngine#expertSearch(java.lang.String, java.util.Locale, int)
	 */
	public ResultSet expertSearch(String cclQuery,Locale locale,int searchingView) throws ModCatalogingException
	{
		try {
			logger.info("Connecting to SE: "+getQGateHostname()+":"+getQGatePort()+" searching for '"+cclQuery+"' lang="+locale+" and usrvw="+searchingView);
			setSocket(new Socket(getQGateHostname(), getQGatePort()));
			logger.info("Connected. Searching for '"+cclQuery+"' lang="+locale+" and usrvw="+searchingView+"...");
			new FindRequest(cclQuery, locale, searchingView, userProfile).send(getSocket());
			FindResponse response = new FindResponse();
			response.recv(getSocket());
			getSocket().close();
			response.checkReturnCode();
			if (response.getResultCount() == 0) {
				throw new NoResultsFoundException();
			}
			logger.info("Found "+response.getResultCount()+" results");
			return new AmicusResultSet(this, searchingView, cclQuery, response.getRecords());
		} catch (UnknownHostException e) {
			logger.error("Unknown Host for qGate ("+getQGateHostname()+":"+getQGatePort()+")", e);
			ConnectException ce = new ConnectException();
			ce.initCause(e);
			throw ce;
		/*} catch (SocketTimeoutException e) {
			logger.error("Time out connecting to qGate ("+getQGateHostname()+":"+getQGatePort()+")", e);
			/*ConnectException ce = new ConnectException();
			ce.initCause(e);
			throw ce;*/
		}catch (IOException e) {
			logger.error("Connection to qGate failed ("+getQGateHostname()+":"+getQGatePort()+")", e);
			ConnectException ce = new ConnectException();
			ce.initCause(e);
			throw ce;
		} finally {
			try {
				if(!getSocket().isClosed()) {
					getSocket().close();
				}
			} catch (IOException e) {
				logger.error("error closing socket: "+e);
				// do nothing 
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.searching.SearchEngine#fetchRecords(org.folio.cataloging.business.searching.ResultSet, java.lang.String, int, int)
	 */
//	public void fetchRecords(
//		ResultSet rs,
//		String elementSetName,
//		int firstRecord,
//		int lastRecord) {
//		AmicusResultSet ars = (AmicusResultSet) rs;
//		Colgate colgate = new Colgate();
//		ConversionOutRequest request =
//			new ConversionOutRequest(
//				// MIKE For the conversionOut, archives and bibliographics have the same behaviour
//				ars.isBibliographic() || ars.isArchive(),
//				-1,
//				ars.getSearchingView(),
//				elementSetName,
//				librarySymbol);
//		try {
//			colgate.connect();
//			for (int i = firstRecord; i <= lastRecord; i++) {
//				request.setAmicusNumber(ars.getAmicusNumbers()[i - 1]);
//				request.send(colgate.getSocket());
//				ConversionOutResponse response = new ConversionOutResponse();
//				response.recv(colgate.getSocket());
//				Record aRecord = ars.getRecord()[i - 1];
//				if (aRecord == null) {
//					aRecord = new MarcRecord();
//					ars.setRecord(i - 1, aRecord);
//				}
//				((MarcRecord) aRecord).setContent(
//					elementSetName,
//					response.getMarcRecord());
//				/*
//				 * Note that when the return code from colgate is non-zero
//				 * the Marc record will be "".  This in turn will be translated
//				 * into an <error/> element in the xml and displayed appropriately
//				 */
//			}
//			colgate.disconnect();
//		} catch (Exception e) {
//			//TODO handle errors in records so that user sees something meaningful
//			//	for now unprocessed records remain null
//			return;
//		}
//	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.folio.cataloging.business.searching.SearchEngine#fetchRecords(librisuite.business
	 * .searching.ResultSet, java.lang.String, int, int)
	 */
	public void fetchRecords(ResultSet rs, String elementSetName,
			int firstRecord, int lastRecord) {
		logger.debug("fetchRecords()");
		AmicusResultSet ars = (AmicusResultSet) rs;
		Colgate colgate = new Colgate();
		DAOCache daoCache = new DAOCache();
		ConversionOutRequest request = new ConversionOutRequest(
		// MIKE For the conversionOut, archives and bibliographics have the same
		// behaviour
				ars.isBibliographic() || ars.isArchive(), -1, ars
						.getSearchingView(), ELEMENT_SETNAME, librarySymbol);
		try {
			colgate.connect();
			for (int i = firstRecord; i <= lastRecord; i++) {
				request.setAmicusNumber(ars.getAmicusNumbers()[i - 1]);
				/*
				 * pm 2011
				 * The test below is added for the case we are fetching the variants
				 * of a bib item.  The view to be fetched is pre-set in each result set record.
				 */
				if (ars.getRecord()[i-1] != null && ars.getRecord()[i-1].getRecordView() > 0) {
					logger.debug("record " + (i-1) + " exists and has view " + ars.getRecord()[i-1].getRecordView());
					request.setSearchingView(ars.getRecord()[i-1].getRecordView());
				}
				else if (ars.getSearchingView() == View.ANY) {
					int preferredView = daoCache.getPreferredView(request
							.getAmicusNumber(), userProfile
							.getDatabasePreferenceOrder());
					logger.debug("searching ANY, setting preferred view to " +
							preferredView);
							request.setSearchingView(preferredView);
							
				}
				logger.debug("fetching " + request.getAmicusNumber() + " view " + request.getSearchingView());
				request.send(colgate.getSocket());
				ConversionOutResponse response = new ConversionOutResponse();
				response.recv(colgate.getSocket());
				Record aRecord = ars.getRecord()[i - 1];
				if (aRecord == null) {
					aRecord = new MarcRecord();
					ars.setRecord(i - 1, aRecord);
				}
				aRecord.setContent(elementSetName, response
						.getMarcRecord());
				aRecord.setRecordView(request.getSearchingView());
				/*
				 * Note that when the return code from colgate is non-zero the
				 * Marc record will be "". This in turn will be translated into
				 * an <error/> element in the xml and displayed appropriately
				 */
			}
			logger.info("Searching end");
			colgate.disconnect();
		} catch (Exception e) {
			// TODO handle errors in records so that user sees something
			// meaningful
			// for now unprocessed records remain null
			logger.debug("Exception thrown", e);
			return;
		}
	}



	private String getLocalisedOperator(String code, Locale locale) {
		String[] results = (String[]) operators.get(locale);
		logger.debug("looking for boolean operator " + code);
		if (results == null) {
			results = new String[6];
			results[0] = "";
			ResourceBundle bundle =
				ResourceBundle.getBundle(
					"resources/searching/advancedSearch",
					locale);
			results[1] = bundle.getString("and");
			results[2] = bundle.getString("or");
			results[3] = bundle.getString("not");
			results[4] = bundle.getString("near");
			results[5] = bundle.getString("with");
			operators.put(locale, results);
		}
		logger.debug("returning " + results[Integer.parseInt(code)]);
		return results[Integer.parseInt(code)];
	}

	/**
	 * 
	 * @since 1.0
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUserProfile(UserProfile profile) {
		userProfile = profile;
		try {
			librarySymbol =
				new DAOLibrary()
					.load(userProfile.getMainLibrary())
					.getLibrarySymbolCode();
		} catch (DataAccessException e) {
			logger.warn(
				"No library symbol found for org "
					+ userProfile.getMainLibrary());
		}
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.searching.SearchEngine#simpleSearch(java.lang.String, java.lang.String, java.util.Locale, int)
	 */
	public ResultSet simpleSearch(
		String query,
		String use,
		Locale locale,
		int searchingView)
		throws ModCatalogingException {
		String cclQuery = buildCclQuery(query, use, locale);
		return expertSearch(cclQuery, locale, searchingView);
	}

	/* (non-Javadoc)
	 * @see org.folio.cataloging.business.searching.SearchEngine#sort(org.folio.cataloging.business.searching.ResultSet, java.lang.String[], java.lang.String[])
	 */
	public void sort(ResultSet rs, String[] attributes, String[] directions)
		throws ModCatalogingException {
		new DAOSortResultSets().sort(
			(AmicusResultSet) rs,
			attributes,
			directions);
		rs.clearRecords();
	}

	public void setQGateHostname(String gateHostname) {
		qGateHostname = gateHostname;
	}

	public void setQGatePort(int gatePort) {
		qGatePort = gatePort;
	}

}
