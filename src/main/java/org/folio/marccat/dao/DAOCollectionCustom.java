package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.persistence.CollectionCustomer;
import org.folio.marccat.dao.persistence.CollectionCustomerArch;
import org.folio.marccat.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DAOCollectionCustom extends AbstractDAO {
  private static final String SORT_NAME_ITA_CODE = "nameIta";
  private static final String SORT_TYPOLOGY_CODE = "typologyCode";
  private static final String SORT_STATUS_CODE = "statusCode";
  private static final String SORT_ASC_CODE = "ASC";
  private static Log logger = LogFactory.getLog(DAOCollectionCustom.class);

  public DAOCollectionCustom() {
    super();
  }


  /**
   * 20110223: Aggiunta scrittura della tabella archivio per le custom eleminate
   * e gestito tutto in modo che se tutto ok commit altrimenti rollback
   */
  public void delete(CollectionCustomer collectionCustomer, String user) throws DataAccessException {
    Session s = currentSession();
    Transaction tx = null;
    try {
      tx = s.beginTransaction();
      s.delete(collectionCustomer);
      deleteCollName(collectionCustomer.getNameIta());
      CollectionCustomerArch collectionCustomerArch = new CollectionCustomerArch(collectionCustomer, user);
      collectionCustomerArch.markNew();
      s.save(collectionCustomerArch);
      tx.commit();
    } catch (HibernateException e) {
      logAndWrap(e);
      try {
        tx.rollback();
      } catch (HibernateException e1) {
        logAndWrap(e1);
      }
    }
  }

  /**
   * Metodo che cancella il nome della collection eliminata
   *
   * @param nameIta
   * @throws DataAccessException
   */
  public void deleteCollName(int nameIta) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    Session session = currentSession();
    String query = "";
    try {
      connection = session.connection();
      query = "delete from T_CLCTN_CST_TYP i where i.TBL_VLU_CDE = ? ";
      stmt = connection.prepareStatement(query);
      stmt.setInt(1, nameIta);
      stmt.execute();
    } catch (HibernateException e) {
      logAndWrap(e);
    } catch (SQLException e1) {
      logAndWrap(e1);
    } finally {
      try {
        if (stmt != null) stmt.close();
      } catch (SQLException e) {
        logAndWrap(e);
      }
    }
  }
}
