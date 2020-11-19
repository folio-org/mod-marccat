package org.folio.marccat.dao;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.folio.marccat.business.cataloguing.authority.AuthorityCatalog;
import org.folio.marccat.business.cataloguing.authority.AuthorityItem;
import org.folio.marccat.business.cataloguing.authority.AuthorityTagImpl;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.Authority008Tag;
import org.folio.marccat.dao.persistence.AuthorityAuthenticationCodeTag;
import org.folio.marccat.dao.persistence.AuthorityCataloguingSourceTag;
import org.folio.marccat.dao.persistence.AuthorityControlNumberTag;
import org.folio.marccat.dao.persistence.AuthorityDateOfLastTransactionTag;
import org.folio.marccat.dao.persistence.AuthorityHeadingTag;
import org.folio.marccat.dao.persistence.AuthorityLeader;
import org.folio.marccat.dao.persistence.AuthorityNote;
import org.folio.marccat.dao.persistence.AuthorityReferenceTag;
import org.folio.marccat.dao.persistence.CatalogItem;
import org.folio.marccat.dao.persistence.Descriptor;
import org.folio.marccat.dao.persistence.EquivalenceReference;
import org.folio.marccat.dao.persistence.FULL_CACHE;
import org.folio.marccat.dao.persistence.REF;
import org.folio.marccat.dao.persistence.ReferenceType;
import org.folio.marccat.dao.persistence.SeeAlsoReferenceTag;
import org.folio.marccat.dao.persistence.SeeReferenceTag;
import org.folio.marccat.dao.persistence.T_DUAL_REF;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.ModMarccatException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.util.XmlUtils;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

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
    try {
      return getAuthorityItemByAmicusNumber(key[0], session);
    } catch (HibernateException e) {
      return null;
    }
  }

  private AuthorityItem getAuthorityItem(int id, Session session) {

    AuthorityItem item = new AuthorityItem();

    AUT autItm = null;

    try {
      autItm = new AutDAO().load(session, id);
    } catch (HibernateException e) {
      throw new ModMarccatException(e);
    }

    item.setAutItmData(autItm);
    return item;
  }

  public List<Tag> getHeaderFields(final AuthorityItem item) {

    final List<Tag> result = new ArrayList<>();

    result.add(new AuthorityLeader());
    result.add(new AuthorityControlNumberTag());
    result.add(new AuthorityDateOfLastTransactionTag());
    result.add(new Authority008Tag());
    result.add(new AuthorityCataloguingSourceTag());
    if (item.getItemEntity().getAuthenticationCenterStringText() != null) {
      result.add(new AuthorityAuthenticationCodeTag());
    }
    return result;
  }

  private AuthorityHeadingTag getHeadingField(AuthorityItem item, Session session) throws HibernateException {
    AUT autData = item.getAutItmData();
    AuthorityHeadingTag result = AuthorityCatalog.createHeadingTagByType(autData.getHeadingType());
    result.setItemEntity(autData);
    Descriptor heading = AuthorityCatalog.getDaoByType(autData.getHeadingType()).load(autData.getHeadingNumber(), View.DEFAULT_BIBLIOGRAPHIC_VIEW, session);
    if (heading == null) {
      throw new DataAccessException();
    }
    result.setDescriptor(heading);
    return result;
  }

  public AuthorityItem getAuthorityItemByAmicusNumber(int amicusNumber, Session session) throws HibernateException {
    AuthorityItem item = getAuthorityItem(amicusNumber, session);
    item.addAllTags(getHeaderFields(item).toArray(new Tag[0]));
    AuthorityHeadingTag heading = getHeadingField(item, session);
    item.addTag(heading);
    item.addAllTags(getReferenceFields(item, heading, session).toArray(new Tag[0]));
  
    item.addAllTags(getNotes(amicusNumber, session).toArray(new Tag[0]));
    Iterator<Tag> iter = item.getTags().iterator();
    while (iter.hasNext()) {
      Tag ff = (Tag) iter.next();
      ff.setTagImpl(new AuthorityTagImpl());
      if (ff instanceof PersistsViaItem) {
        ((PersistsViaItem) ff).setItemEntity(item.getItemEntity());
      }
    }
    return item;
  }

  public List<Tag> getReferenceFields(AuthorityItem item, AuthorityHeadingTag heading, Session session) throws HibernateException {
    List<Tag> result = new ArrayList<>();
    Descriptor d = heading.getDescriptor();
    DescriptorDAO dao = (DescriptorDAO) d.getDAO();
    List<REF> refs = dao.getCrossReferences(heading.getDescriptor(), View.DEFAULT_BIBLIOGRAPHIC_VIEW, session);
    Iterator<REF> iter = refs.iterator();
    while (iter.hasNext()) {
      REF anXref = (REF) iter.next();
      int refType = anXref.getType();
      if (ReferenceType.isAuthorityTag(refType)) {

        AuthorityReferenceTag t;
        if (ReferenceType.isEquivalence(refType)) {
          t = new EquivalenceReference();
        } else if (ReferenceType.isSeenFrom(refType)) {
          t = new SeeReferenceTag();
        } else {
          t = new SeeAlsoReferenceTag();
          REF dual = (REF) anXref.copy();
          dual.setType(ReferenceType.getReciprocal(anXref.getType()));
          if (refs.contains(dual)) {
            ((SeeAlsoReferenceTag) t).setDualReferenceIndicator(T_DUAL_REF.YES);
          }
        }

        t.setReference(anXref);
        t.setDescriptor(anXref.getTargetDAO().load(anXref.getTarget(), View.DEFAULT_BIBLIOGRAPHIC_VIEW, session));
        result.add(t);
      }
    }
    return result;
  }

  private List<AuthorityNote> getNotes(final int amicusNumber, final Session session) throws HibernateException {
    return session.find("from AuthorityNote as an where an.itemNumber = ? ", new Object[] { amicusNumber },
        new Type[] { Hibernate.INTEGER });
  }

}
