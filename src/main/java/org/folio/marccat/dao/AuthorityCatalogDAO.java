package org.folio.marccat.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.authority.AuthorityCatalog;
import org.folio.marccat.business.cataloguing.authority.AuthorityItem;
import org.folio.marccat.business.cataloguing.authority.AuthorityTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.AuthorityHeadingTag;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.FULL_CACHE;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.util.XmlUtils;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * The class manages the authority record
 * 
 * @author elena
 *
 */
public class AuthorityCatalogDAO extends CatalogDAO {

  public static final Log logger = LogFactory.getLog(AuthorityCatalogDAO.class);

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
    ((AuthorityItem) item).sortTags(session);
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
    int id = key[0];

    try {
      return getAuthorityItemByAmicusNumber(id, session);
    } catch (final HibernateException exception) {
      throw new DataAccessException(exception);
    }
  }

  private CatalogItem getAuthorityItemByAmicusNumber(int id, Session session) throws HibernateException {
    AuthorityItem item = getAuthorityItem(id, session);

    AuthorityHeadingTag heading = getHeadingField(item, session);
    item.addTag(heading);

    for (Tag ff : item.getTags()) {
      ff.setTagImpl(new AuthorityTagImpl());
      if (ff instanceof PersistsViaItem) {
        ((PersistsViaItem) ff).setItemEntity(item.getItemEntity());
      }
    }

    return item;
  }

  private AuthorityHeadingTag getHeadingField(AuthorityItem item, final Session session) throws HibernateException {
    AUT autData = item.getAutItmData();
    AuthorityHeadingTag result = AuthorityCatalog.createHeadingTagByType(autData.getHeadingType());
    result.setItemEntity(autData);
    Descriptor heading = AuthorityCatalog.getDaoByType(autData.getHeadingType()).load(autData.getHeadingNumber(),
        View.DEFAULT_BIBLIOGRAPHIC_VIEW, session);
    if (heading == null) {
      logger.warn("No heading found for authority item");
      throw new DataAccessException();
    }
    result.setDescriptor(heading);
    return result;
  }

  private AuthorityItem getAuthorityItem(int id, Session session) throws HibernateException {
    AuthorityItem item = new AuthorityItem();

    AUT autItm = new AutDAO().load(session, id);
    item.setAutItmData(autItm);
    return item;
  }

  @Override
  public void deleteCatalogItem(final CatalogItem item, final Session session) throws HibernateException {
    final Transaction transaction = getTransaction(session);

    /*
     * item.getTags().stream().filter(aTag -> aTag instanceof
     * Persistence).forEach(tag -> { try { session.delete(tag); } catch
     * (HibernateException e) { cleanUp(transaction); throw new
     * ModMarccatException(e); } });
     */

    item.getTags().stream().filter(aTag -> aTag instanceof AuthorityHeadingTag).forEach(tag -> {
      AuthorityHeadingTag authorityHeadingTag = (AuthorityHeadingTag) tag;
      Descriptor authorityHeading = authorityHeadingTag.getDescriptor();
      try {
        authorityHeading.getDAO().delete(authorityHeading, session);
      } catch (HibernateException e) {
        cleanUp(transaction);
        throw new ModMarccatException(e);
      }
    });

    new AutDAO().delete(item.getItemEntity(), session);
    transaction.commit();
  }

}
