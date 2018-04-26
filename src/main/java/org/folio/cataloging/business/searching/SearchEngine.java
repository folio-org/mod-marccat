/*
 * (c) LibriCore
 * 
 * Created on May 30, 2006
 * 
 * SearchEngine.java
 */
package org.folio.cataloging.business.searching;

import org.folio.cataloging.business.controller.UserProfile;
import org.folio.cataloging.exception.ModCatalogingException;

import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Locale;

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
		throws ModCatalogingException;
	public abstract ResultSet simpleSearch(
            String query,
            String use,
            Locale locale,
            int searchingView)
		throws ModCatalogingException;

	public abstract ResultSet advancedSearch(
            List termList,
            List relationList,
            List useList,
            List operatorList,
            Locale locale,
            int searchingView)
		throws NoResultsFoundException, ModCatalogingException,SocketTimeoutException;

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
            String[] directions) throws ModCatalogingException;

	public void setUserProfile(UserProfile userProfile);
}
