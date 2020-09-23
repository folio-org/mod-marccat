package org.folio.marccat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.folio.marccat.business.cataloguing.authority.AuthorityItem;
import org.folio.marccat.business.cataloguing.authority.AuthorityTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.Authority008Tag;
import org.folio.marccat.dao.persistence.AuthorityAuthenticationCodeTag;
import org.folio.marccat.dao.persistence.AuthorityCataloguingSourceTag;
import org.folio.marccat.dao.persistence.AuthorityControlNumberTag;
import org.folio.marccat.dao.persistence.AuthorityDateOfLastTransactionTag;
import org.folio.marccat.dao.persistence.AuthorityGeographicAreaTag;
import org.folio.marccat.dao.persistence.AuthorityLeader;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.FULL_CACHE;
import org.folio.marccat.exception.DataAccessException;
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

	/**
	 * Gets bibliographic item corresponding to bibliographic record.
	 *
	 * @param amicusNumber -- the amicus number of item.
	 * @param userView     -- user view associated.
	 * @param session      -- the current session hibernate.
	 * @return the bibliographic item by amicus number.
	 * @throws HibernateException in case of hibernate exception.
	 */
	public AuthorityItem getAuthorityItemByAmicusNumber(final int amicusNumber, final int userView,
			final Session session) throws HibernateException {

		AuthorityItem item = getAuthorityItem(amicusNumber, session);
		item.getTags().addAll(getHeaderFields(item));
		item.getTags().forEach(tag -> {
			tag.setTagImpl(new AuthorityTagImpl());
			tag.setCorrelationKey(tag.getTagImpl().getMarcEncoding(tag, session));
		});
		item.sortTags();
		return item;
	}

	private AuthorityItem getAuthorityItem(int id, Session session) throws HibernateException {
		AuthorityItem item = new AuthorityItem();
		AUT autItm = new AutDAO().load(session, id);
		item.setAutItmData(autItm);
		return item;
	}

	private List<Tag> getHeaderFields(final AuthorityItem item) {

		final AUT autItemData = item.getAutItmData();
		final List<Tag> result = new ArrayList<>();
		result.add(new AuthorityLeader());
		result.add(new AuthorityControlNumberTag());
		result.add(new AuthorityDateOfLastTransactionTag());
		result.add(new Authority008Tag());
		result.add(new AuthorityCataloguingSourceTag());

		if (item.getItemEntity().getAuthenticationCenterStringText() != null) {
			result.add(new AuthorityAuthenticationCodeTag());
		}
		if (item.getItemEntity().getGeographicAreaStringText() != null) {
			result.add(new AuthorityGeographicAreaTag());
		}
		return result.stream().map(tag -> {
			if (tag instanceof PersistsViaItem)
				((PersistsViaItem) tag).setItemEntity(autItemData);

			return tag;
		}).collect(Collectors.toList());

	}

	@Override
	public CatalogItem getCatalogItemByKey(Session session, int... key) {
		int id = key[0];
		int cataloguingView = key[1];

		try {
			return getAuthorityItemByAmicusNumber(id, cataloguingView, session);
		} catch (final HibernateException exception) {
			throw new DataAccessException(exception);
		}
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

}
