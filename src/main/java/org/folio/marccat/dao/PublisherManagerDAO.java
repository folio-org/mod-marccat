package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.persistence.PUBL_TAG;
import org.folio.marccat.dao.persistence.PublisherAccessPoint;
import org.folio.marccat.dao.persistence.PublisherManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Although PublisherManager implements Persistence, it is in fact not mapped to
 * a table through Hibernate. Instead it delegates persistence to its
 * constituent access point and PUBL_TAGs
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class PublisherManagerDAO extends AbstractDAO {


  public void delete(Persistence po, final Session session) throws DataAccessException {
    if (!(po instanceof PublisherManager)) {
      throw new IllegalArgumentException(
        "I can only persist PublisherManager objects");
    }
    PublisherManager aPub = (PublisherManager) po;
    PublisherAccessPoint apf = aPub.getApf();
    apf.markDeleted();

    try {
      persistByStatus(apf, session);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }


  public void save(final Persistence po, final Session session) throws DataAccessException {
    if (!(po instanceof PublisherManager)) {
      throw new IllegalArgumentException(
        "I can only persist PublisherManager objects");
    }
    Transaction tx = null;
    try {
      tx = getTransaction(session);

      int tagNumber = getNextPublisherTagNumber(session);
      PublisherManager aPub = (PublisherManager) po;
      PublisherAccessPoint apf = aPub.getApf();

      List<PUBL_TAG> publTags = aPub.getPublisherTagUnits();
      publTags.forEach(ptag -> {
        try {
          session.evict(ptag);
          ptag.markNew();
          ptag.setPublisherTagNumber(tagNumber);
          persistByStatus(ptag, session);
        } catch (HibernateException e) {
          throw new DataAccessException(e);
        }
      });

      session.evict(apf);
      apf.markNew();
      apf.setHeadingNumber(new Integer(tagNumber));
      persistByStatus(apf, session);

      tx.commit();
    } catch (HibernateException exception) {
      cleanUp(tx);
      throw new DataAccessException(exception);
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see HibernateUtil#update(librisuite.business.common.Persistence)
   */
  public void update(final Persistence p, final Session session) throws DataAccessException {
    if (!(p instanceof PublisherManager)) {
      throw new IllegalArgumentException(
        "Can only persist PublisherManager objects");
    }
    Transaction tx = null;
    try {
      tx = getTransaction(session);
      PublisherManager aPub = (PublisherManager) p;
      PublisherAccessPoint apf = aPub.getApf();
      apf.markDeleted();
      persistByStatus(apf, session);
      session.flush();
      session.evict(apf);
      apf.markNew();
      save(p, session);
      tx.commit();
    } catch (HibernateException exception) {
      cleanUp(tx);
      throw new DataAccessException(exception);
    }
  }

  public int getNextPublisherTagNumber(final Session session) throws DataAccessException {
    int result = 0;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
      stmt = session.connection().prepareStatement("SELECT nextval('publ_tag_seq')");
      rs = stmt.executeQuery();
      rs.next();
      result = rs.getInt(1);
    } catch (SQLException | HibernateException e) {
      throw new DataAccessException(e);
    } finally {
      try {
        if (stmt != null) stmt.close();
        if (rs != null) rs.close();
      } catch (SQLException e) {
        throw new DataAccessException(e);
      }
    }
    return result;
  }
}
