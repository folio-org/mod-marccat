package org.folio.cataloging.search;

import net.sf.hibernate.HibernateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.amicusSearchEngine.AmicusResultSet;
import org.folio.cataloging.business.amicusSearchEngine.AmicusSearchEngine;
import org.folio.cataloging.business.cataloguing.common.Catalog;
import org.folio.cataloging.business.cataloguing.common.CatalogItem;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.business.librivision.Record;
import org.folio.cataloging.business.librivision.XmlRecord;
import org.folio.cataloging.business.searching.NoResultsFoundException;
import org.folio.cataloging.business.searching.ResultSet;
import org.folio.cataloging.dao.CatalogDAO;
import org.folio.cataloging.dao.DAOCache;
import org.folio.cataloging.dao.DAOFullCache;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.FULL_CACHE;
import org.folio.cataloging.exception.ModCatalogingException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 2018 Paul Search Engine Java
 * 
 * @author paulm
 * @since 1.0
 */
public class JavaSearchEngine extends AmicusSearchEngine {
	@Override
	public ResultSet expertSearch(String cclQuery, Locale locale, int searchingView) throws NoResultsFoundException, ModCatalogingException
	{
		Connection conn = null;
		PreparedStatement sql = null;
		java.sql.ResultSet rs = null;
		try 
		{
			Parser p = new Parser(locale, userProfile, searchingView);
			conn = new HibernateUtil().currentSession().connection();
			sql = conn.prepareStatement(p.parse(cclQuery));
			rs = sql.executeQuery();
			ArrayList<Integer> results = new ArrayList<Integer>();
			while (rs.next()) 
			{
				results.add(rs.getInt(1));
			}
			if (results.size() == 0) {
				throw new NoResultsFoundException();
			}
			int[] intResults = new int[results.size()];
			for (int i = 0; i < intResults.length; i++) {
				intResults[i] = results.get(i).intValue();
			}
			return new AmicusResultSet(this, searchingView, cclQuery, intResults);
			
		} catch (HibernateException e) {
			throw new DataAccessException(e.getMessage());
		} catch (SQLException e) {
			throw new DataAccessException(e.getMessage());
		} finally {
			if (rs != null)  { try { rs.close();  } catch (SQLException e) {logger.error("error closing resultSet: " + e);} }
			if (sql != null) { try { sql.close(); } catch (SQLException e) {logger.error("error closing statement: " + e);} }
		}
	}

	private static final Log logger = LogFactory.getLog(JavaSearchEngine.class);

	@Override
	public void fetchRecords(ResultSet rs, String elementSetName, int firstRecord, int lastRecord) 
	{
		logger.debug("fetchRecords()");
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
				if (ars.getRecord()[i - 1] != null	&& ars.getRecord()[i - 1].getRecordView() > 0) 
				{
					logger.debug("record " + (int) (i - 1) + " exists and has view " + ars.getRecord()[i - 1].getRecordView());
					searchingView = ars.getRecord()[i - 1].getRecordView();
				
				} else if (ars.getSearchingView() == View.ANY)
				{
					int preferredView = daoCache.getPreferredView(itemNumber, userProfile.getDatabasePreferenceOrder());
					logger.debug("searching ANY, setting preferred view to " + preferredView);
					searchingView = preferredView;
				}
				
				logger.debug("fetching " + itemNumber + " view " + searchingView);
				FULL_CACHE cache = null;
				try {
					cache = new DAOFullCache().load(itemNumber, searchingView);
				} catch (RecordNotFoundException e) {
					try 
					{
						Catalog theCatalog = Catalog.getInstanceByView(searchingView);
						CatalogDAO theDao = theCatalog.getCatalogDao();
						CatalogItem theItem = theDao.getCatalogItemByKey(new Object[] { itemNumber, searchingView });
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
			logger.info("Searching end");
		} catch (Exception e) {
			// TODO handle errors in records so that user sees something meaningful for now unprocessed records remain null
			logger.debug("Exception thrown", e);
			return;
		}
	}
}
