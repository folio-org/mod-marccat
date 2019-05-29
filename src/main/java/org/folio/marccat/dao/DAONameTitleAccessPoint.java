package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.dao.persistence.NME_TTL_HDG;
import org.folio.marccat.dao.persistence.NameAccessPoint;
import org.folio.marccat.dao.persistence.NameTitleAccessPoint;
import org.folio.marccat.dao.persistence.TitleAccessPoint;
import org.folio.marccat.exception.DataAccessException;

/**
 * Data access object to Name-Title access point.
 *
 * @author paulm
 * @author nbianchini
 * @since 1.0
 */
public class DAONameTitleAccessPoint extends AbstractDAO {


  /**
   * Delete the access point entries in NME_TITLE_ACS_PNT, NME_ACS_PNT and TTL_ACS_PNT
   *
   * @param p the persistence object
   * @param session the session
   * @throws HibernateException in case of data access failure.
   */
  @Override
  public void delete(final Persistence p, final Session session) throws HibernateException {
   Transaction tx = null;
    try{
      super.delete(p, session);
      tx = getTransaction(session);
      final NameTitleAccessPoint a = (NameTitleAccessPoint) p;

      session.delete(
          "from NameAccessPoint as n "
            + " where n.nameTitleHeadingNumber = ? and "
            + " n.userViewString = ? ",
          new Object[]{a.getHeadingNumber(), a.getUserViewString()},
          new Type[]{Hibernate.INTEGER, Hibernate.STRING});

      session.delete(
          "from TitleAccessPoint as n "
            + " where n.nameTitleHeadingNumber = ? and "
            + " n.userViewString = ? ",
          new Object[]{a.getHeadingNumber(), a.getUserViewString()},
          new Type[]{Hibernate.INTEGER, Hibernate.STRING});

      tx.commit();

  } catch (HibernateException exception) {
    cleanUp(tx);
    throw new DataAccessException(exception);
  }
  }

  /**
   * Save the access point entries in NME_TITLE_ACS_PNT, NME_ACS_PNT and TTL_ACS_PNT
   *
   * @param p the persistence object
   * @param session the session
   * @throws HibernateException in case of data access failure.
   */
  @Override
  public void save(final Persistence p, final Session session) throws HibernateException{
    Transaction tx = null;
    try {
      super.save(p, session);
      tx = getTransaction(session);
      final NameTitleAccessPoint nt = (NameTitleAccessPoint) p;
      final NameAccessPoint a = new NameAccessPoint(nt.getItemNumber());
      a.setNameTitleHeadingNumber(nt.getHeadingNumber().intValue());
      a.setHeadingNumber((((NME_TTL_HDG) nt.getDescriptor()).getNameHeadingNumber()));
      a.setUserViewString(nt.getUserViewString());
      a.setFunctionCode((short) 0);
      persistByStatus(a, session);
      TitleAccessPoint b = new TitleAccessPoint(nt.getItemNumber());
      b.setNameTitleHeadingNumber(nt.getHeadingNumber().intValue());
      b.setHeadingNumber((((NME_TTL_HDG) nt.getDescriptor()).getTitleHeadingNumber()));
      b.setUserViewString(nt.getUserViewString());
      b.setFunctionCode((short) 0);
      persistByStatus(b, session);
      tx.commit();

    } catch (HibernateException exception) {
      cleanUp(tx);
      throw new DataAccessException(exception);
    }
  }


}
