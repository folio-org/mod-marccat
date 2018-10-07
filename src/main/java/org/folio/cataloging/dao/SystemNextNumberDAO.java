package org.folio.cataloging.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.S_NXT_NBR;

/**
 * Class representing the access to S_NXT_NBR table
 *
 * @author Maite
 * @author nbianchini
 * @since 1.0
 */
public class SystemNextNumberDAO extends AbstractDAO {

	/*@Deprecated
	public int getNextNumber(final String keyFieldCodeValue) throws DataAccessException {
		throw new IllegalArgumentException("DON'T CALL ME!");
	}*/

  @Deprecated
  public int getPreviouwsNumber(final String keyFieldCodeValue) throws DataAccessException {
    throw new IllegalArgumentException ("DON'T CALL ME!");
  }

  /**
   * This method return the field updated nextNumber and save changes in the table.
   *
   * @param keyFieldCodeValue -- key of the row to increase
   * @return the increased number.
   * @throws HibernateException in case of Hibernate exception.
   */
  public int getNextNumber(final String keyFieldCodeValue, final Session session) throws HibernateException {

    final S_NXT_NBR snn = (S_NXT_NBR) get (session, S_NXT_NBR.class, keyFieldCodeValue, LockMode.UPGRADE);
    final int nextNbr = snn.getKeyFieldNextNumber ( );

    final Transaction transaction = getTransaction (session);
    try {

      int i = snn.getKeyFieldNextNumber ( ) + 1;
      snn.setKeyFieldNextNumber (i);
      session.update (snn);
      transaction.commit ( );

    } catch (Exception e) {
      cleanUp (transaction);
      throw new HibernateException (e);
    }

    return nextNbr;
  }

  /**
   * This method return the field updated previous number and save changes in the table.
   *
   * @param keyFieldCodeValue -- key of the row to decrease.
   * @return the decreased number.
   * @throws HibernateException in case of Hibernate exception.
   */
  public int getPreviousNumber(final String keyFieldCodeValue, final Session session) throws HibernateException {
    final S_NXT_NBR snn = (S_NXT_NBR) get (session, S_NXT_NBR.class, keyFieldCodeValue, LockMode.UPGRADE);
    final int nextNbr = snn.getKeyFieldNextNumber ( );

    final Transaction transaction = getTransaction (session);
    try {

      int i = snn.getKeyFieldNextNumber ( ) - 1;
      snn.setKeyFieldNextNumber (i);
      session.update (snn);
      transaction.commit ( );

    } catch (Exception e) {
      cleanUp (transaction);
      throw new HibernateException (e);
    }

    return nextNbr;
  }

}
