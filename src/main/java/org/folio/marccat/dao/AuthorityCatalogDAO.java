package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.authority.*;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.View;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.util.XmlUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class AuthorityCatalogDAO extends CatalogDAO {
  public static final Log logger =
    LogFactory.getLog(AuthorityCatalogDAO.class);

  public AuthorityItem getAuthorityItemByAmicusNumber(int id) {
    AuthorityItem item = getAuthorityItem(id);
    AuthorityHeadingTag heading = getHeadingField(item);
    item.addTag(heading);

    Iterator iter = item.getTags().iterator();
    while (iter.hasNext()) {
      Tag ff = (Tag) iter.next();
      ff.setTagImpl(new AuthorityTagImpl());
      if (ff instanceof PersistsViaItem) {
        // set the AUT data portion of the fixed field
        ((PersistsViaItem) ff).setItemEntity(item.getItemEntity());
      }
    }
    if (logger.isDebugEnabled())
      logger.debug(
        "Authority item: "
          + Integer.toString(id)
          + " successfully loaded from DB");
    return item;
  }

  // 2018 Paul Search Engine Java
  @Override
  public void updateFullRecordCacheTable(Session session, CatalogItem item) {
    FULL_CACHE cache;
    FullCacheDAO dao = new FullCacheDAO();
    try {
      cache = dao.load(session, item.getAmicusNumber(), View.AUTHORITY);
    } catch (RecordNotFoundException e) {
      cache = new FULL_CACHE();
      cache.setItemNumber(item.getAmicusNumber());
      cache.setUserView(View.AUTHORITY);
    }
    cache.setRecordData(XmlUtils.documentToString(item.toExternalMarcSlim(session)));
  }

  private AuthorityItem getAuthorityItem(int id) {
    if (logger.isDebugEnabled())
      logger.debug("loading authority item: " + Integer.toString(id));

    AuthorityItem item = new AuthorityItem();

    final Session session = currentSession();
    AUT autItm = null;
    try {
      autItm = new AutDAO().load(session, id);
    } catch (HibernateException e) {
      logger.error(e.getMessage());
    }
    item.setAutItmData(autItm);
    return item;
  }

  @Override
  public CatalogItem getCatalogItemByKey(final Session session, final int... key) {
    return getAuthorityItemByAmicusNumber(key[0]);
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
    if (item.getItemEntity().getGeographicAreaStringText() != null) {
      result.add(new AuthorityGeographicAreaTag());
    }
    if (item.getItemEntity().getTypeOfDateTimeCode() != null) {
      result.add(new TimePeriodOfHeading());
    }
    return result;
  }

  private AuthorityHeadingTag getHeadingField(AuthorityItem item) {
    AUT autData = item.getAutItmData();
    AuthorityHeadingTag result =
      AuthorityCatalog.createHeadingTagByType(autData.getHeadingType());
    result.setItemEntity(autData);
    Descriptor heading =
      AuthorityCatalog.getDaoByType(autData.getHeadingType()).load(
        autData.getHeadingNumber(),
        AuthorityCatalog.CATALOGUING_VIEW);
    if (heading == null) {
      logger.warn("No heading found for authority item");
      throw new DataAccessException();
    }
    if (logger.isDebugEnabled()) {
      logger.debug("heading loaded: " + heading);
    }
    result.setDescriptor(heading);
    return result;
  }

  public List<Tag> getReferenceFields(
    AuthorityHeadingTag heading) {
    List<Tag> result = new ArrayList<>();
    Descriptor d = heading.getDescriptor();
    DescriptorDAO dao = (DescriptorDAO) d.getDAO();
    List refs =
      dao.getCrossReferences(
        heading.getDescriptor(),
        AuthorityCatalog.CATALOGUING_VIEW);
    Iterator iter = refs.iterator();
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
          /*
           * set the seeAlso dual reference indicator based on the contents
           * of the database
           */
          REF dual = (REF) anXref.clone();
          dual.setType(ReferenceType.getReciprocal(anXref.getType()));
          if (refs.contains(dual)) {
            ((SeeAlsoReferenceTag) t).setDualReferenceIndicator(
              T_DUAL_REF.YES);
            ((REF) refs.get(refs.indexOf(dual))).evict();
          }
        }

        t.setReference(anXref);
        t.setDescriptor(
          anXref.getTargetDAO().load(
            anXref.getTarget(),
            AuthorityCatalog.CATALOGUING_VIEW));
        result.add(t);
      }
    }
    return result;
  }


  @Override
  protected void updateItemDisplayCacheTable(CatalogItem item, Session session) {
    // do nothing -- Authorities don't have a cache table (yet)
  }


  public List getValidHeadingTypeList(String headingType, Locale locale) {
    return find(
      "select distinct new "
        + "Avp(ct.code, ct.longText) "
        + " from T_AUT_REF_HDG_TYP as ct, AuthorityCorrelation as db "
        + " where ct.code = db.key.marcTagCategoryCode "
        + " and db.key.headingType = ? "
        + " and ct.language = ? ",
      new Object[]{headingType, locale.getISO3Language()},
      new Type[]{Hibernate.STRING, Hibernate.STRING});
  }


  public List findNotes(Integer amicusNumber) {
    List notes = new ArrayList();
    ResultSet rs = null;
    String query = "SELECT * from AUT_NTE where AUT_NBR=" + amicusNumber;
    try {
      Connection con = currentSession().connection();
      rs = con.createStatement().executeQuery(query);
      while (rs.next()) {
        AuthorityNote note = new AuthorityNote();

        note.setItemNumber(rs.getInt("AUT_NBR"));
        note.setNoteNumber(rs.getInt("AUT_NTE_NBR"));
        note.setNoteStringText(rs.getString("AUT_NTE_STRNG_TXT"));

        notes.add(note);
      }
    } catch (Exception e) {
      return notes;
    } finally {
      if (rs != null) try {
        rs.close();
      } catch (SQLException e) {
        logger.error(e.getMessage());
      }
    }
    return notes;
  }
}
