package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.common.SortFormException;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.CPY_ID;
import org.folio.marccat.dao.persistence.SHLF_LIST;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.RecordNotFoundException;

import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public class DAOCopy extends AbstractDAO {


  private static Log logger = new Log(DAOCopy.class);

  private String INSERT_BND_CPY = "INSERT INTO BND_CPY a "
    + "SELECT BIB_ITM_NBR," + "CPY_ID_NBR," + "SHLF_LIST_KEY_NBR,"
    + "ORG_NBR," + "BRNCH_ORG_NBR," + "ORGNL_ORG_NBR," + "BRCDE_NBR,"
    + "DYNIX_SRL_ID_NBR," + "TRSTN_DTE," + "CRTN_DTE," + "ILL_CDE,"
    + "HLDG_SBCPT_STUS_CDE," + "HLDG_RTNTN_CDE," + "LOAN_PRD_CDE,"
    + "HLDG_SRS_TRMT_CDE," + "HLDG_STUS_TYP_CDE," + "LCTN_NME_CDE,"
    + "HLDG_LVL_OF_DTL_CDE," + "HLDG_ACSN_LIST_CDE," + "CPY_NBR_DSC,"
    + "CPY_RMRK_NTE," + "CPY_STMT_TXT," + "CPY_RMRK_NTE_SRT_FORM,"
    + "TMP_LCTN_ORG_NBR," + "TMP_LCTN_NME_CDE," + "MTRL_DESC,"
    + "CST, " + "CURCY_TYP_CDE, " + "CURCY_XCHNG_RTE,"
    + "TRSFR_CSTDY_NBR, " + "PHSCL_CPY_TPE,"
    + "MTHD_ACQ FROM cpy_id b WHERE b.cpy_id_nbr=?";

  private String INSERT_BND_SHLF_LIST = "INSERT INTO BND_SHLF_LIST "
    + "SELECT ORG_NBR,SHLF_LIST_KEY_NBR,SHLF_LIST_TYP_CDE,SHLF_LIST_STRNG_TEXT,SHLF_LIST_SRT_FORM FROM SHLF_LIST A WHERE A.SHLF_LIST_KEY_NBR=?";

  /**
   * Loads copy by copy number.
   *
   * @param session    -- the hibernate session associated to request.
   * @param copyNumber -- the copy number.
   * @return {@link CPY_ID}
   * @throws DataAccessException in case of hibernate exception.
   */
  public CPY_ID load(final Session session, final int copyNumber) throws DataAccessException {
    CPY_ID c = null;
    try {
      c = (CPY_ID) session.get(CPY_ID.class, copyNumber);
      if (c != null) {
        if (c.getShelfListKeyNumber() != null) {
          c.setShelfList(new ShelfListDAO().load(c.getShelfListKeyNumber(), session));
        }
      }

      // todo: to manage from external configuration module based on orgNumber
      /*if ((new DAOGlobalVariable ( ).getValueByName ("barrcode")).equals ("1")) {
        c.setBarcodeAssigned (true);
      } else {
        c.setBarcodeAssigned (false);
      }*/

    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
    return c;
  }


  /**
   * @param copyBarCode is the barCode of the copy
   * @return the BibItemNumber from CPY_ID table
   * @since 1.0
   */

  /**
   * Gets the amicus number associated to copy.
   *
   * @param session -- the hibernate session associated to request.
   * @param barCode -- the barcode associated to copy.
   * @return the amicus number.
   * @throws DataAccessException in case of hibernate exception.
   */
  public int getBibItemNumber(final Session session, final String barCode) throws DataAccessException {
    int result = 0;

    try {
      List<CPY_ID> listAllCopies = session.find("from CPY_ID ci where ci.barCodeNumber = '" + barCode + "'");
      return listAllCopies.stream().filter(Objects::nonNull).reduce((first, second) -> second).get().getBibItemNumber();

    } catch (HibernateException e) {
      throw new DataAccessException(e);
      //log error and return 0?
    }

  }

  /**
   * @param copyIdNumber is the copyIdNumber of the copy
   * @return the BibItemNumber from CPY_ID table
   * @since 1.0
   */

  public int getBibItemNumber(int copyIdNumber) throws DataAccessException,
    HibernateException {
    int result = 0;

    List listAllCopies = null;
    try {
      Session s = currentSession();
      listAllCopies = s.find("from CPY_ID ci where ci.copyIdNumber = "
        + copyIdNumber);

      Iterator iter = listAllCopies.iterator();
      while (iter.hasNext()) {
        CPY_ID rawCopy = (CPY_ID) iter.next();
        result = rawCopy.getBibItemNumber();
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    }

    return result;
  }


  public void delete(final int copyNumber, final String userName) throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException,
        DataAccessException {
        // TODO make sure no circulation records (AMICUS doesn't)
        CPY_ID copy = (CPY_ID) s.get(CPY_ID.class, new Integer(
          copyNumber));
        if (copy.getShelfListKeyNumber() != null) {
          //TODO passare la session
          copy.setShelfList(new ShelfListDAO().load(copy.getShelfListKeyNumber().intValue(), null));
        }
        if (copy == null) {
          throw new RecordNotFoundException();
        }

        // detach the shelflist
        detachShelfList(copy, copy.getShelfList());

        saveCpyIdAgent(userName, copy.getCopyIdNumber());

        // delete the copy itself
        s.delete(copy);


        DAOSummaryHolding ds = new DAOSummaryHolding();
        ds.deleteRecord(copy.getBibItemNumber(), copy
          .getOrganisationNumber());


      }
    }.execute();
  }

  public void detachShelfList(CPY_ID copy, SHLF_LIST shelf)
    throws DataAccessException {
    Session s = currentSession();

    if (shelf == null) {
      return;
    }
    try {
      /*
       * If only our copies bib_item is using this shelf list then remove
       * the entry from SHLF_LIST_ACS_PNT
       */

      if (countShelfFromCopyUses(copy, shelf) != 0) {
        if (countShelfListAccessPointUses(copy, shelf) == 1) {
          logger.info("Cancella  SHLF_LIST_ACS_PNT");

          s
            .delete(
              "from SHLF_LIST_ACS_PNT as c where c.shelfListKeyNumber = ?"
                + " and c.bibItemNumber = ?",
              new Object[]{
                new Integer(shelf
                  .getShelfListKeyNumber()),
                new Integer(copy.getBibItemNumber())},
              new Type[]{Hibernate.INTEGER,
                Hibernate.INTEGER});
          /*
           * AND if only our copy is using this shelf list number then
           * delete the shelf list number
           */
          List l = find(
            "select count(*) from CPY_ID as c where c.shelfListKeyNumber = ?",
            new Object[]{new Integer(shelf
              .getShelfListKeyNumber())},
            new Type[]{Hibernate.INTEGER});
          if (l.size() > 0 && ((Integer) l.get(0)).intValue() == 1) {
            s.delete(shelf);
          }
        }
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    }
  }

  /**
   * Counts the number of copies using this shelf list and bib_itm
   *
   * @since 1.0
   */
  public int countShelfListAccessPointUses(CPY_ID copy, SHLF_LIST shelf)
    throws DataAccessException {
    List l = find(
      "select count(*) from CPY_ID as c where c.shelfListKeyNumber = ?"
        + " and c.bibItemNumber = ?", new Object[]{
        new Integer(shelf.getShelfListKeyNumber()),
        new Integer(copy.getBibItemNumber())}, new Type[]{
        Hibernate.INTEGER, Hibernate.INTEGER});
    if (l.size() > 0) {
      return ((Integer) l.get(0)).intValue();
    } else {
      return 0;
    }
  }

  /**
   * Counts the number of copies using this shelf list and bib_itm
   *
   * @since 1.0
   */
  public int countShelfFromCopyUses(CPY_ID copy, SHLF_LIST shelf)
    throws DataAccessException {
    List l = find(
      "select count(*) from CPY_ID as c where c.shelfListKeyNumber = ?"
        + " and c.bibItemNumber = ?"
        + " and c.copyIdNumber = ?", new Object[]{
        new Integer(shelf.getShelfListKeyNumber()),
        new Integer(copy.getBibItemNumber()),
        new Integer(copy.getCopyIdNumber())},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER,
        Hibernate.INTEGER});
    if (l.size() > 0) {
      return ((Integer) l.get(0)).intValue();
    } else {
      return 0;
    }
  }


  public String calculateSortForm(String text, SortFormParameters parms)
    throws DataAccessException {
    String result = "";
    int bufSize = 600;
    int rc;

    Session s = currentSession();
    CallableStatement proc = null;
    Connection connection = null;

    try {
      connection = s.connection();
      proc = connection
        .prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_PREPROCESS(?, ?, ?, ?, ?, ?, ?, ?) }");
      proc.registerOutParameter(1, Types.INTEGER);
      proc.setString(2, text);
      proc.registerOutParameter(3, Types.VARCHAR);
      proc.setInt(4, bufSize);
      proc.setInt(5, parms.getSortFormMainType());
      proc.setInt(6, parms.getSortFormSubType());
      proc.setInt(7, parms.getNameTitleOrSubjectType());
      proc.setInt(8, parms.getNameSubtype());
      proc.setInt(9, parms.getSkipInFiling());
      proc.execute();

      rc = proc.getInt(1);

      if (rc != 0) {
        throw new SortFormException(String.valueOf(rc));
      }
      result = proc.getString(3);

      proc.close();

      proc = connection
        .prepareCall("{ ? = call AMICUS.PACK_SORTFORM.SF_BUILDSRTFRM(?, ?, ?, ?, ?, ?, ?, ?) }");
      proc.registerOutParameter(1, Types.INTEGER);
      proc.setString(2, result);
      proc.registerOutParameter(3, Types.VARCHAR);
      proc.setInt(4, bufSize);
      proc.setInt(5, parms.getSortFormMainType());
      proc.setInt(6, parms.getSortFormSubType());
      proc.setInt(7, parms.getNameTitleOrSubjectType());
      proc.setInt(8, parms.getNameSubtype());
      proc.setInt(9, parms.getSkipInFiling());
      proc.execute();

      rc = proc.getInt(1);

      if (rc != 0) {
        throw new SortFormException(String.valueOf(rc));
      }
      result = proc.getString(3);
    } catch (HibernateException e) {
      logAndWrap(e);
    } catch (SQLException e) {
      logAndWrap(e);
    } finally {
      try {
        if (proc != null) {
          proc.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return result;
  }


  public void saveCpyIdAgent(String userName, int cpyIdNbr) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt0 = null;
    PreparedStatement stmt1 = null;
    PreparedStatement stmt2 = null;
    Session session = currentSession();
    int count = 0;
    try {
      connection = session.connection();
      // --------> Lock della riga
      stmt0 = connection.prepareStatement("SELECT * FROM CPY_ID_AGENT WHERE CPY_ID_NBR = ? FOR UPDATE");
      stmt0.setInt(1, cpyIdNbr);

      stmt1 = connection.prepareStatement("UPDATE CPY_ID_AGENT SET USERNAME = ?, TRSTN_DTE = SYSDATE , AGENT_ID = 1 WHERE CPY_ID_NBR = ?");
      stmt1.setString(1, userName);
      // stmt1.setDate(2, new java.sql.Date(System.currentTimeMillis()));
      stmt1.setInt(2, cpyIdNbr);
      count = stmt1.executeUpdate();

      if (!(count > 0)) {
        stmt2 = connection.prepareStatement("INSERT INTO CPY_ID_AGENT (CPY_ID_NBR, USERNAME, AGENT_ID, TRSTN_DTE) VALUES (?,?,?,SYSDATE)");
        stmt2.setInt(1, cpyIdNbr);
        stmt2.setString(2, userName);
        stmt2.setInt(3, 1);
        // stmt2.setDate(4, new
        // java.sql.Date(System.currentTimeMillis()));
        stmt2.execute();
      }
      /* Il commit o rollback lo fa hibernate in automatico se le operazioni successive vanno bene: quindi se sulla CPY_ID va tutto ok committa altrimenti no */
      // connection.commit();
    } catch (HibernateException e) {
      e.printStackTrace();
      logAndWrap(e);
      // try {
      // connection.rollback();
      // } catch (SQLException e1) {
      // e1.printStackTrace();
      // }
    } catch (SQLException e) {
      e.printStackTrace();
      logAndWrap(e);
      // try {
      // connection.rollback();
      // } catch (SQLException e1) {
      // e1.printStackTrace();
      // }
    } finally {
      try {
        if (stmt0 != null) {
          stmt0.close();
        }
        if (stmt1 != null) {
          stmt1.close();
        }
        if (stmt2 != null) {
          stmt2.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
        logAndWrap(e);
      }
    }
  }

}
