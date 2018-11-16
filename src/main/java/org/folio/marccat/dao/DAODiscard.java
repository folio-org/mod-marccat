/*
 * (c) LibriCore
 *
 * Created on Jan 24, 2005
 *
 * DAOInventory.java
 */
package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.CPY_ID;
import org.folio.marccat.dao.persistence.SHLF_LIST;
import org.folio.marccat.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2005/02/02 14:09:42 $
 * @since 1.0
 */
public class DAODiscard extends DAOCopy {

  /**
   * @param copyNumber
   * @param discardTyp
   * @throws DataAccessException
   */
  public void save(final SHLF_LIST shelf, final int copyNumber, final int discardTyp) throws DataAccessException {

    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException,
        DataAccessException {
        CPY_ID copy = (CPY_ID) s.get(CPY_ID.class, new Integer(
          copyNumber));
        copy.setShelfList(shelf);
        if (copy.getShelfListKeyNumber() != null) {
          copy.setShelfListKeyNumber(0);
        }
        // detach the shelflist
        detachShelfList(copy, copy.getShelfList());
        // tabella DISCARD_CPY
        saveDiscard(copy, discardTyp);
        //if(copy.getShelfListKeyNumber()==0)
        s.update(copy);

      }
    }.execute();
  }


  /**
   * @param cpy
   * @param discardTyp
   * @throws DataAccessException
   */
  public void saveDiscard(CPY_ID cpy, int discardTyp) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    Session session = currentSession();
    //SAVE se non esiste il record
    try {
      connection = session.connection();
      stmt = connection.prepareStatement("INSERT INTO DSCRD_CPY (BIB_ITM_NBR, CPY_ID_NBR , ORG_NBR, BRNCH_ORG_NBR, DSCRD_CDE, DSCRD_DTE, LCTN_NME_CDE ) VALUES (?,?,?,?,?,SYSDATE,?)");
      stmt.setInt(1, cpy.getBibItemNumber());
      stmt.setInt(2, cpy.getCopyIdNumber());
      stmt.setInt(3, cpy.getOrganisationNumber());
      stmt.setInt(4, cpy.getBranchOrganisationNumber());
      stmt.setInt(5, discardTyp);
      stmt.setInt(6, cpy.getLocationNameCode());
      stmt.execute();
      //Update se esiste gia il record solo delle due info DSCRD_CDE, DSCRD_DTE
    } catch (HibernateException e) {
      e.printStackTrace();
      logAndWrap(e);

    } catch (SQLException e) {
      e.printStackTrace();
      logAndWrap(e);
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
        logAndWrap(e);
      }
    }
  }

}
