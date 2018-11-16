package org.folio.marccat.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.marccat.business.codetable.Avp;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.common.RecordNotFoundException;
import org.folio.marccat.business.common.SortFormException;
import org.folio.marccat.business.common.UpdateStatus;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.copycataloguing.CopyListElement;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.*;
import org.folio.marccat.log.Log;
import org.folio.marccat.util.StringText;

import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.folio.marccat.F.deepCopy;
import static org.folio.marccat.F.fixedCharPadding;

@SuppressWarnings("unchecked")
public class DAOCopy extends AbstractDAO {

  public static final Comparator<CPY_ID> CPY_ID_COMPARATOR =
    (CPY_ID o1, CPY_ID o2) -> {
      int i1 = o1.getCopyIdNumber();
      int i2 = o2.getCopyIdNumber();
      if (i1 == i2) return 0;
      else if (i1 < i2) return -1;
      return 1;
    };

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

  /**
   * @param copy
   * @return
   * @throws DataAccessException
   */
  //TODO: The session is missing from the method
  public SHLF_LIST getMatchHeading(CPY_ID copy) throws DataAccessException, HibernateException {
    ShelfListDAO ds = (ShelfListDAO) copy.getShelfList().getDAO();
    SHLF_LIST match = (SHLF_LIST) ds
      .getMatchingHeading(copy.getShelfList(), null);
    return match;
  }

  private void createSummaryHolding(final Session session, final CPY_ID copy) throws DataAccessException {
    new DAOSummaryHolding().createSummaryHoldingIfRequired(session, copy);
  }

  public void attachShelfList(CPY_ID copy, SHLF_LIST shelf)
    throws DataAccessException {
    if (countShelfListAccessPointUses(copy, shelf) == 0) {
      // logger.warn("Attacca SHLF_LIST_ACS_PNT");
      try {
        SHLF_LIST_ACS_PNT ap = new SHLF_LIST_ACS_PNT(copy
          .getBibItemNumber(), copy.getOrganisationNumber(),
          shelf.getShelfListKeyNumber());
        currentSession().save(ap);
      } catch (HibernateException e) {
        logAndWrap(e);
      }
    }
  }

  public void attachShelfListForModifyCopy(CPY_ID copy, SHLF_LIST shelf)
    throws DataAccessException {
    if (countShelfFromCopyUses(copy, shelf) != 0) {
      if (countShelfListAccessPointUses(copy, shelf) == 1) {
        // logger.warn("Attacca SHLF_LIST_ACS_PNT");
        try {
          SHLF_LIST_ACS_PNT ap = new SHLF_LIST_ACS_PNT(copy
            .getBibItemNumber(), copy.getOrganisationNumber(),
            shelf.getShelfListKeyNumber());
          currentSession().save(ap);
        } catch (HibernateException e) {
          logAndWrap(e);
        }
      }
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



  public void insertHardbackTable(List<CopyListElement> elements,
                                  String posseduto, Integer value) {

    Connection connection = null;
    PreparedStatement stmt2 = null;
    try {
      connection = currentSession().connection();

      for (CopyListElement element : elements) {
        stmt2 = connection
          .prepareStatement("INSERT INTO HLDG_CPY_ACS_PNT(HLDG_NBR, CPY_ID_NBR) VALUES (?, ?)");
        stmt2.setInt(1, value);
        stmt2.setInt(2, element.getCopy().getCopyIdNumber());
        stmt2.executeUpdate();
      }

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {
      try {
        stmt2.close();

      } catch (SQLException e) {
      }
    }
  }



  public void removeHardbackHLDG_CPY_ACS_PNT(Integer value) {

    Connection connection = null;
    PreparedStatement stmt2 = null;
    try {
      connection = currentSession().connection();

      stmt2 = connection
        .prepareStatement("DELETE HLDG_CPY_ACS_PNT  WHERE HLDG_NBR=?");
      stmt2.setInt(1, value);
      stmt2.executeUpdate();

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {
      try {
        stmt2.close();
      } catch (SQLException e) {
      }
    }
  }


  public void deleteTemporaryCopiesFromCPY_IDTable(Integer hldg_nbr) {
    Connection connection = null;
    PreparedStatement stmt = null;
    try {
      connection = currentSession().connection();

      stmt = connection
        .prepareStatement("DELETE CPY_ID WHERE CPY_ID_NBR IN (SELECT CPY_ID_NBR FROM HLDG_CPY_ACS_PNT WHERE HLDG_NBR=? )");
      stmt.setInt(1, hldg_nbr);
      stmt.executeUpdate();

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {
      try {
        stmt.close();
      } catch (SQLException e) {
      }
    }
  }

  public void updatedefiniteCopyHLDG(Integer cpy_id, Integer hldg_nbr) {

    Connection connection = null;
    PreparedStatement stmt = null;
    try {
      connection = currentSession().connection();

      stmt = connection
        .prepareStatement("UPDATE HLDG SET BND_VOL_CPY_ID=? WHERE HLDG_NBR=?");
      stmt.setInt(1, cpy_id);
      stmt.setInt(2, hldg_nbr);
      stmt.executeUpdate();

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {
      try {
        stmt.close();
      } catch (SQLException e) {
      }
    }

  }

  public void deleteHardbackShelflistKey(Integer hldg_nbr) {

    Connection connection = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt2 = null;
    PreparedStatement stmt3 = null;
    PreparedStatement stmt4 = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    try {
      connection = currentSession().connection();

      stmt = connection
        .prepareStatement("SELECT A.SHLF_LIST_KEY_NBR FROM CPY_ID A WHERE A.CPY_ID_NBR IN (SELECT CPY_ID_NBR FROM HLDG_CPY_ACS_PNT WHERE HLDG_NBR=?)");
      stmt.setInt(1, hldg_nbr);
      rs = stmt.executeQuery();

      while (rs.next()) {
        int key = rs.getInt("SHLF_LIST_KEY_NBR");

        stmt2 = connection
          .prepareStatement("SELECT count(*) FROM CPY_ID A WHERE A.SHLF_LIST_KEY_NBR=?");
        stmt2.setInt(1, key);
        rs2 = stmt2.executeQuery();
        int count = 0;
        if (rs2.next())
          count = rs2.getInt(1);

        if (count == 1) {
          stmt3 = connection
            .prepareStatement("DELETE FROM SHLF_LIST WHERE SHLF_LIST_KEY_NBR=?");
          stmt3.setInt(1, key);
          stmt3.executeUpdate();

          stmt4 = connection
            .prepareStatement("DELETE FROM SHLF_LIST_ACS_PNT WHERE SHLF_LIST_KEY_NBR=?");
          stmt4.setInt(1, key);
          stmt4.executeUpdate();
        } else if (count > 1) {
          stmt4 = connection
            .prepareStatement("DELETE FROM SHLF_LIST_ACS_PNT WHERE SHLF_LIST_KEY_NBR=?");
          stmt4.setInt(1, key);
          stmt4.executeUpdate();
        }

      }

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {
      try {
        rs.close();
      } catch (SQLException e) {
      }
      try {
        if (rs2 != null)
          rs2.close();
      } catch (SQLException e) {
      }
      try {
        stmt.close();
      } catch (SQLException e) {
      }
      try {
        if (stmt2 != null)
          stmt2.close();
      } catch (SQLException e) {
      }
      try {
        if (stmt3 != null)
          stmt3.close();
      } catch (SQLException e) {
      }
      try {
        if (stmt4 != null)
          stmt4.close();
      } catch (SQLException e) {
      }

    }

  }

  public void insertBND_CPY(Integer hldg_nbr) {

    Connection connection = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt1 = null;
    ResultSet rs = null;
    try {
      connection = currentSession().connection();

      connection = currentSession().connection();
      stmt = connection
        .prepareStatement("SELECT CPY_ID_NBR FROM HLDG_CPY_ACS_PNT WHERE HLDG_NBR=?");
      stmt.setInt(1, hldg_nbr);

      rs = stmt.executeQuery();

      while (rs.next()) {
        int cpy_id_nbr = rs.getInt("CPY_ID_NBR");
        stmt1 = connection.prepareStatement(INSERT_BND_CPY);
        stmt1.setInt(1, cpy_id_nbr);
        stmt1.executeUpdate();
      }

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {

      try {
        rs.close();
      } catch (SQLException e) {
      }
      try {
        stmt.close();
      } catch (SQLException e) {
      }
      try {
        if (stmt1 != null)
          stmt1.close();
      } catch (SQLException e) {
      }
    }
  }

  public void insertBND_SHLF_LIST(Integer hldg_nbr) {

    Connection connection = null;
    PreparedStatement stmt = null;
    PreparedStatement stmt1 = null;
    ResultSet rs = null;
    try {
      connection = currentSession().connection();

      connection = currentSession().connection();
      stmt = connection
        .prepareStatement("SELECT A.SHLF_LIST_KEY_NBR FROM CPY_ID A WHERE A.CPY_ID_NBR IN (SELECT CPY_ID_NBR FROM HLDG_CPY_ACS_PNT WHERE HLDG_NBR=?)");
      stmt.setInt(1, hldg_nbr);
      rs = stmt.executeQuery();
      while (rs.next()) {
        int shlfListKey = rs.getInt("SHLF_LIST_KEY_NBR");
        if (!isBNDShelfList(shlfListKey)) {
          stmt1 = connection.prepareStatement(INSERT_BND_SHLF_LIST);
          stmt1.setInt(1, shlfListKey);
          stmt1.executeUpdate();
        }
      }

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {

      try {
        rs.close();
      } catch (SQLException e) {
      }
      try {
        stmt.close();
      } catch (SQLException e) {
      }
      try {
        if (stmt1 != null)
          stmt1.close();
      } catch (SQLException e) {
      }
    }

  }

  private boolean isBNDShelfList(int shelfListKey) {

    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      connection = currentSession().connection();

      connection = currentSession().connection();
      stmt = connection
        .prepareStatement("SELECT A.SHLF_LIST_KEY_NBR FROM BND_SHLF_LIST A WHERE A.SHLF_LIST_KEY_NBR=?");
      stmt.setInt(1, shelfListKey);
      rs = stmt.executeQuery();

      return rs.next();

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {

      try {
        rs.close();
      } catch (SQLException e) {
      }
      try {
        stmt.close();
      } catch (SQLException e) {
      }
    }
    return false;
  }


  /**
   * Il metodo controllo se ci sono copie (non da trasferire) associate al
   * record origine per l'org
   *
   * @param amicusNumber
   * @param orgNumber
   * @param condition
   * @return
   * @throws DataAccessException
   */
  public int countCopies(int amicusNumber, int orgNumber, String condition)
    throws DataAccessException {
    List l = find("select count(*) from CPY_ID as c"
      + " where c.organisationNumber = ?"
      + " and c.bibItemNumber = ?" + " and c.copyIdNumber "
      + condition, new Object[]{new Integer(orgNumber),
      new Integer(amicusNumber)}, new Type[]{Hibernate.INTEGER,
      Hibernate.INTEGER});

    if (l.size() > 0) {
      return ((Integer) l.get(0)).intValue();
    } else {
      return 0;
    }
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


  public void removeDefinitiveHldgAndBndCopy(Integer hldgNbr) {

    Connection connection = null;

    PreparedStatement stmt1 = null;
    PreparedStatement stmt2 = null;

    try {
      connection = currentSession().connection();
      stmt2 = connection
        .prepareStatement("DELETE HLDG  WHERE HLDG_NBR=?");
      stmt2.setInt(1, hldgNbr);
      stmt2.executeUpdate();

      stmt1 = connection
        .prepareStatement("DELETE HLDG_BND_CPY_ACS_PNT WHERE HLDG_NBR=?");
      stmt1.setInt(1, hldgNbr);
      stmt1.executeUpdate();

    } catch (Exception e) {
      try {
        logAndWrap(e);
      } catch (DataAccessException e1) {
        e1.printStackTrace();
      }
    } finally {
      try {
        stmt2.close();
      } catch (SQLException e) {
      }
      try {
        stmt1.close();
      } catch (SQLException e) {
      }
    }
  }

}
