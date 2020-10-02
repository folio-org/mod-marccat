package org.folio.marccat.dao;

import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.FULL_CACHE;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.util.XmlUtils;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * The class manages the authority record
 * 
 * @author elena
 *
 */
public class AuthorityCatalogDAO extends CatalogDAO {

	public AuthorityCatalogDAO() {
		super();
	}

	@Override
	/**
	 * @param session
	 * @param item
	 * @param updateRelatedRecs
	 * @throws HibernateException
	 */
	public void updateFullRecordCacheTable(final Session session, final CatalogItem item) throws HibernateException {
		FULL_CACHE cache;
		FullCacheDAO dao = new FullCacheDAO();
		try {
			cache = dao.load(session, item.getAmicusNumber(), item.getUserView());
		} catch (RecordNotFoundException e) {
			cache = new FULL_CACHE();
			cache.setItemNumber(item.getAmicusNumber());
			cache.setUserView(item.getUserView());
		}
		/**item.sortTags();*/
		cache.setRecordData(XmlUtils.documentToString(item.toExternalMarcSlim(session)));
		cache.markChanged();
		persistByStatus(cache, session);
	}

	@Override
	protected void updateItemDisplayCacheTable(CatalogItem item, Session session) throws HibernateException {
		updateFullRecordCacheTable(session, item);
	}

	@Override
	public CatalogItem getCatalogItemByKey(Session session, int... key) {
		// TODO Auto-generated method stub
		return null;
	}

}
