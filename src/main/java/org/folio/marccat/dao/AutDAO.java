package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordNotFoundException;
import org.folio.marccat.dao.persistence.AUT;

import java.util.List;

/**
 * The Class AutDAO used for authority record.
 *
 * @author paulm
 * @author carment
 * @since 1.0
 */
public class AutDAO extends AbstractDAO {

  /**
   * return a authority record
   *
   * @param session
   * @param id
   * @return
   * @throws DataAccessException
   */
  public AUT load(final Session session, final int id) throws HibernateException {
    AUT itm = (AUT) session.get(AUT.class, new Integer(id));
    if (itm == null) {
      throw new RecordNotFoundException();
    } else {
      return itm;
    }
  }


  /**
   * returns the number of bibliographic records linked to an authority record
   *
   * @param headingNumber
   * @param searchingView
   * @param accessPoint
   * @param session
   * @return the count of bibliographic records
   * @throws HibernateException
   */
  public Integer getDocCountByAutNumber(final int headingNumber, final Class accessPoint, final Integer searchingView, final Session session) throws HibernateException {
    final List countDoc = session.find(" select count(distinct apf.bibItemNumber) from "
      + accessPoint.getName() + " as apf "
      + " where apf.headingNumber = ? and "
      + " substr(apf.userViewString, ?, 1) = '1'", new Object[]{
      headingNumber,
      searchingView}, new Type[]{
      Hibernate.INTEGER, Hibernate.INTEGER});
    return (countDoc.size() > 0) ? (Integer) countDoc.get(0) : 0;
  }


}
