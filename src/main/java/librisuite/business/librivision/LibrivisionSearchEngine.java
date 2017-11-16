/*
 * (c) LibriCore
 * 
 * Created on May 30, 2006
 * 
 * LibrivisionSearchEngine.java
 */
package librisuite.business.librivision;

import java.util.List;
import java.util.Locale;

import librisuite.business.exception.LibrisuiteException;
import librisuite.business.searching.NoResultsFoundException;
import librisuite.business.searching.ResultSet;
import librisuite.business.searching.SearchEngine;

import com.libricore.librisuite.controller.UserProfile;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public class LibrivisionSearchEngine implements SearchEngine {

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public LibrivisionSearchEngine() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see librisuite.business.searching.SearchEngine#expertSearch(java.lang.String, java.util.Locale, int)
	 */
	public ResultSet expertSearch(
		String cclQuery,
		Locale locale,
		int searchingView)
		throws LibrisuiteException {
		ResultSet result =
			LVMessage.LVCclSearch(locale, cclQuery, searchingView);
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.searching.SearchEngine#simpleSearch(java.lang.String, java.lang.String, java.util.Locale, int)
	 */
	public ResultSet simpleSearch(
		String query,
		String use,
		Locale locale,
		int searchingView)
		throws LibrisuiteException {
		ResultSet result =
			LVMessage.LVSimpleSearch(locale, query, use, searchingView);
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.searching.SearchEngine#advancedSearch(java.util.List, java.util.List, java.util.List, java.util.List, java.util.Locale, int)
	 */
	public ResultSet advancedSearch(
		List termList,
		List relationList,
		List useList,
		List operatorList,
		Locale locale,
		int searchingView)
		throws NoResultsFoundException, LibrisuiteException {
		ResultSet result =
			LVMessage.LVAdvancedSearch(
				locale,
				termList,
				relationList,
				useList,
				operatorList,
				searchingView);
		return result;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.searching.SearchEngine#setUserProfile(com.libricore.librisuite.controller.UserProfile)
	 */
	public void setUserProfile(UserProfile userProfile) {
		// do nothing -- Librivision uses a static user-id for connections	
	}

	/* (non-Javadoc)
	 * @see librisuite.business.searching.SearchEngine#fetchRecords(librisuite.business.searching.ResultSet, java.lang.String, int, int)
	 */
	public void fetchRecords(
		ResultSet rs,
		String elementSetName,
		int firstRecord,
		int lastRecord) {
		LVMessage.LVViewRecords(
			(LVResultSet) rs,
			elementSetName,
			firstRecord,
			lastRecord);

	}

	/* (non-Javadoc)
	 * @see librisuite.business.searching.SearchEngine#sort(librisuite.business.searching.ResultSet, java.lang.String[], java.lang.String[])
	 */
	public void sort(ResultSet rs, String[] attributes, String[] directions)
		throws LibrisuiteException {
		LVMessage.LVSort((LVResultSet) rs, attributes, directions);
	}

}
