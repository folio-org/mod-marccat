/*
 * (c) LibriCore
 * 
 * Created on Aug 6, 2004
 * 
 * $Author: Paulm $
 * $Date: 2006/03/17 08:27:57 $
 * $Locker:  $
 * $Name:  $
 * $Revision: 1.11 $
 * $Source: /source/LibriSuite/src/librisuite/business/common/DAOBibliographicCorrelation.java,v $
 * $State: Exp $
 */
package librisuite.business.common;

import java.util.Iterator;
import java.util.List;

import librisuite.business.cataloguing.mades.MadesCorrelation;
import librisuite.business.searching.DAOIndexList;
import librisuite.hibernate.Correlation;
import librisuite.hibernate.CorrelationKey;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.type.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Manages access to table S_BIB_MARC_IND_DB_CRLTN -- the correlation between AMICUS
 * database encoding and MARC21 encoding
 * @author paulm
 * TODO _MIKE change DAOMadesCorrelation for mades
 * @version $Revision: 1.11 $, $Date: 2006/03/17 08:27:57 $
 */
public class DAOMadesCorrelation extends DAOCorrelation {
	private static final Log logger = LogFactory.getLog(DAOMadesCorrelation.class);

	private static final String MADES_CORRELATION_CACHE_NAME = "man.MadesCorrelation";
	/**
	 * Returns the BibliographicCorrelation from BibliographicCorrelationKey
	 * @param correlationKey -- the database bibliographicCorrelationKey
	 * @return a BibliographicCorrelation object containing or null when none found
	 *
	 */
	public Correlation getCorrelation(CorrelationKey correlationKey)
		throws DataAccessException {
		return getCorrelation(
			correlationKey.getMarcTag(),
			correlationKey.getMarcFirstIndicator(),
			correlationKey.getMarcSecondIndicator(),
			correlationKey.getMarcTagCategoryCode());
	}

	/**
	 * Returns the MadesCorrelation based on MARC encoding and category code
	 * @param tag -- marc tag
	 * @param firstIndicator -- marc first indicator
	 * @param secondIndicator -- marc second indicator
	 * @param categoryCode -- category code
	 * @return a MadesCorrelation object or null when none found
	 *
	 */
	public Correlation getCorrelation(
		String tag,
		char firstIndicator,
		char secondIndicator,
		short categoryCode)
		throws DataAccessException {
		// MIKE: chaged
		List l =
			find(
				"from MadesCorrelation as bc "
					+ "where bc.key.marcTag = ? and "
					+ "bc.key.marcFirstIndicator = ? and "
//					Natascia 13/06/2007: scommentate chiocciole
					+ "bc.key.marcFirstIndicator <> '@' and "
					+ "bc.key.marcSecondIndicator = ? and "
					+ "bc.key.marcSecondIndicator <> '@' and "
					+ "bc.key.marcTagCategoryCode = ?",
				new Object[] {
					new String(tag),
					new Character(firstIndicator),
					new Character(secondIndicator),
					new Short(categoryCode)},
				new Type[] {
					Hibernate.STRING,
					Hibernate.CHARACTER,
					Hibernate.CHARACTER,
					Hibernate.SHORT });

		if (l.size() == 1) {
			return (Correlation) l.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @deprecated use different getMarcEncoding(...)
	 * Returns the MARC encoding based on the input database encodings
	 * @param category -- the database category (1-name, etc...)
	 * @param value1 -- the first database code
	 * @param value2 -- the second database code
	 * @param value3 -- the third database code
	 * @return a MadesCorrelationKey object containing 
	 * the MARC encoding (tag and indicators) or null when none found
	 *
	 */
	public CorrelationKey getMarcEncoding(boolean uncached,
		short category,
		short value1,
		short value2,
		short value3)
		throws DataAccessException {

		List l =
			find(
				"from MadesCorrelation as bc "
					+ "where bc.key.marcTagCategoryCode = ? and "
					+ "bc.databaseFirstValue = ? and "
					+ "bc.databaseSecondValue = ? and "
					+ "bc.databaseThirdValue = ?",
				new Object[] {
					new Short(category),
					new Short(value1),
					new Short(value2),
					new Short(value3)},
				new Type[] {
					Hibernate.SHORT,
					Hibernate.SHORT,
					Hibernate.SHORT,
					Hibernate.SHORT });

		if (l.size() == 1) {
			return ((Correlation) l.get(0)).getKey();
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the cached MARC encoding based on the input database encodings
	 * @param category -- the database category (1-name, etc...)
	 * @param value1 -- the first database code
	 * @param value2 -- the second database code
	 * @param value3 -- the third database code
	 * @return a MadesCorrelationKey object containing 
	 * the MARC encoding (tag and indicators) or null when none found
	 *
	 */
	public CorrelationKey getMarcEncoding(
		short category,
		short value1,
		short value2,
		short value3)
		throws DataAccessException {

		CorrelationValuesKey key = new CorrelationValuesKey(category, value1, value2, value3);
		
		CorrelationKey foundInCache = findObject(key);
		
		if(foundInCache!=null){
			return foundInCache;
		}
		
		try {
			final Query q = currentSession().createQuery(
					"from MadesCorrelation as bc " 
					+ "where bc.key.marcTagCategoryCode = :cat " 
					+ " and bc.databaseFirstValue =  :v1 " 
					+ " and bc.databaseSecondValue = :v2 "
					+ " and bc.databaseThirdValue =  :v3"
			)
			.setShort("cat", category)
			.setShort("v1", value1)
			.setShort("v2", value2)
			.setShort("v3", value3);

			// try to cache...			
//			q.setCacheable(true);
//			q.setCacheRegion("query.MadesCorrelation");
			
			Iterator l = q.iterate();
			if (l.hasNext()) {
				Correlation valueFound = (Correlation) l.next();

				cacheObject(key, valueFound);

				return (valueFound).getKey();
			}
		} catch (HibernateException e) {
			logAndWrap(e);
		}
		return null;
	}

	private CorrelationKey findObject(CorrelationValuesKey key) {
		Element hit;
		try {
			hit = getCache().get(key);
			if(hit==null) return null;
			return ((Correlation) hit.getValue()).getKey();
		} catch (CacheException e) {
			logger.error(e);
			// do nothing, read the DB object
		}
		return null;
	}

	private void cacheObject(CorrelationValuesKey key, Correlation valueFound)  {
		Element toCache = new Element(key, valueFound);
		try {
			getCache().put(toCache);
		} catch (CacheException e) {
			logger.error(e);
			// do nothing, do not cache this object
		}
	}
	
	private Cache getCache() throws CacheException {
		CacheManager cm = CacheManager.getInstance();
		if(!cm.cacheExists(MADES_CORRELATION_CACHE_NAME)) {
			// TODO _MIKE: test using direct cache
			cm.addCache(new Cache(MADES_CORRELATION_CACHE_NAME, 2100, false, true, 30000, 30000));
		}
		return cm.getCache(MADES_CORRELATION_CACHE_NAME);
	}

	public List getSecondCorrelationList(
		short category,
		short value1,
		Class codeTable)
		throws DataAccessException {

		return find(
			" select distinct ct from "
				+ codeTable.getName()
				+ " as ct, MadesCorrelation as bc "
				+ " where bc.key.marcTagCategoryCode = ? and "
//				Natascia 13/06/2007: scommentate chiocciole
				+ " bc.key.marcFirstIndicator <> '@' and "
				+ " bc.key.marcSecondIndicator <> '@' and "
				+ " bc.databaseFirstValue = ? and "
				+ " bc.databaseSecondValue = ct.code and  "
				/*barbara modifica 27/02/2007 non visualizzo nella lista gli obsoleti
				 * PRN 041*/
				+ "ct.obsoleteIndicator = 0  order by ct.sequence ",
			new Object[] { new Short(category), new Short(value1)},
			new Type[] { Hibernate.SHORT, Hibernate.SHORT });
	}

	public List getThirdCorrelationList(
		short category,
		short value1,
		short value2,
		Class codeTable)
		throws DataAccessException {

		return find(
			" select distinct ct from "
				+ codeTable.getName()
				+ " as ct, MadesCorrelation as bc "
				+ " where bc.key.marcTagCategoryCode = ? and "
               //Natascia 13/06/2007: scommentate chiocciole					
				+ " bc.key.marcFirstIndicator <> '@' and "
				+ " bc.key.marcSecondIndicator <> '@' and "
				+ " bc.databaseFirstValue = ? and "
				+ " bc.databaseSecondValue = ? and "					
				+ " bc.databaseThirdValue = ct.code and "
               //Natascia 25/06/2007: non visualizzo nella lista gli obsoleti
				+ " ct.obsoleteIndicator = 0  order by ct.sequence ",
		   new Object[] {
				new Short(category),
				new Short(value1),
				new Short(value2)},
		  new Type[] { Hibernate.SHORT, Hibernate.SHORT, Hibernate.SHORT });
	}

	public short getFirstAllowedValue2(
			short category,
			short value1,
			short value3)
			throws DataAccessException {

			List l = find(
				" from MadesCorrelation as bc "
					+ " where bc.key.marcTagCategoryCode = ? and "
//					Natascia 13/06/2007: scommentate chiocciole					
					+ " bc.key.marcFirstIndicator <> '@' and "
					+ " bc.key.marcSecondIndicator <> '@' and "
					+ " bc.databaseFirstValue = ? and "
					+ " bc.databaseThirdValue = ? ",
				new Object[] {
					new Short(category),
					new Short(value1),
					new Short(value3)},
				new Type[] { Hibernate.SHORT, Hibernate.SHORT, Hibernate.SHORT });
			
			if (l.size() > 0) {
				return ((MadesCorrelation)l.get(0)).getDatabaseSecondValue();
			}
			else {
				return -1;
			}
		}

	public String getClassificationIndexByShelfType(short shelfType)
		throws DataAccessException {
		List l =
			find(
				"from MadesCorrelation as bc "
					+ " where bc.key.marcTagCategoryCode = 13 and "
					+ " bc.databaseFirstValue = ? ",
				new Object[] { new Short(shelfType)},
				new Type[] { Hibernate.SHORT });
		if (l.size() == 1) {
			String s = ((Correlation) l.get(0)).getSearchIndexTypeCode();
			return new DAOIndexList().getIndexByEnglishAbreviation(s);
		} else {
			return null;
		}
	}
	/*modifica barbara 13/04/2007 PRN 127 - nuova intestazione su lista vuota default maschera inserimento intestazione nome*/
	public CorrelationKey getMarcTagCodeBySelectedIndex(String selectedIndex)
	throws DataAccessException {
	List l =
		find(
			"from MadesCorrelation as bc "
				+ " where bc.searchIndexTypeCode = ?" 
				+" or bc.searchIndexTypeCode = ?" ,
	new Object[] { new String(selectedIndex.substring(0, 2)),
				new String(selectedIndex.substring(0, 2).toLowerCase())},
	new Type[] { Hibernate.STRING,  Hibernate.STRING});
	
	if(l.size()>0)
		return ((Correlation) l.get(0)).getKey();
	else	
		return null;
	}

}
