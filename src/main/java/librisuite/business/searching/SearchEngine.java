/*
 * (c) LibriCore
 * 
 * Created on May 30, 2006
 * 
 * SearchEngine.java
 */
package librisuite.business.searching;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Locale;

import librisuite.business.exception.LibrisuiteException;

import com.libricore.librisuite.controller.UserProfile;

/**
 * An interface for generic "AMICUS" search facilities -- currently implemented only as
 * LVMessage (LibriVision)
 * 
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/07/26 14:31:51 $
 * @since 1.0
 */
public interface SearchEngine {
	public abstract ResultSet expertSearch(
            String cclQuery,
            Locale locale,
            int searchingView)
		throws LibrisuiteException;
	public abstract ResultSet simpleSearch(
            String query,
            String use,
            Locale locale,
            int searchingView)
		throws LibrisuiteException;

	public abstract ResultSet advancedSearch(
            List termList,
            List relationList,
            List useList,
            List operatorList,
            Locale locale,
            int searchingView)
		throws NoResultsFoundException, LibrisuiteException,SocketTimeoutException;

	/**
	 * @param firstRecord index of first record to be fetched (starting at 1)
	 * 
	 * @since 1.0
	 */
	public abstract void fetchRecords(
            ResultSet rs,
            String elementSetName,
            int firstRecord,
            int lastRecord);

	public abstract void sort(
            ResultSet rs,
            String[] attributes,
            String[] directions) throws LibrisuiteException;

	public void setUserProfile(UserProfile userProfile);
}
