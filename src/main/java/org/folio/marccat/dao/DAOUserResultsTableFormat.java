package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.Global;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.model.ConfigurationBrowseColumn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.folio.marccat.F.fixedCharPadding;

public class DAOUserResultsTableFormat extends HibernateUtil {
  public String getResultsTableFormatByUser(String userName) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String formatUser = "";

    try {
      connection = currentSession().connection();
      stmt = connection.prepareStatement("SELECT ID_FRMT FROM  " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".USER_RESULTS_FORMAT WHERE USR_NME = ?");
      stmt.setString(1, fixedCharPadding(userName, 12));
      rs = stmt.executeQuery();
      while (rs.next()) {
        formatUser = (rs.getString("ID_FRMT"));
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    } catch (SQLException e) {
      logAndWrap(e);
    } finally {
      try {
        rs.close();
      } catch (SQLException e) {
        logAndWrap(e);
      }
      try {
        stmt.close();
      } catch (SQLException e) {
        logAndWrap(e);
      }
    }
    return formatUser;
  }


  public void SaveResultsTableFormatByUser(String userName, String format) throws DataAccessException {
    if ("".equalsIgnoreCase(getResultsTableFormatByUser(userName))) {
      InsertResultsTableFormatByUser(userName, format);
    } else {
      UpdateResultsTableFormatByUser(userName, format);
    }
  }


  private void InsertResultsTableFormatByUser(String userName, String format) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    Session session = currentSession();
    try {
      connection = session.connection();
      stmt = connection.prepareStatement("INSERT INTO  " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".USER_RESULTS_FORMAT (USR_NME,ID_FRMT) VALUES (?,?)");
      stmt.setString(1, fixedCharPadding(userName, 12));
      stmt.setString(2, format);
      stmt.executeUpdate();
    } catch (HibernateException e) {
      logAndWrap(e);
    } catch (SQLException e) {
      logAndWrap(e);
    } finally {
      try {
        stmt.close();
      } catch (SQLException e) {
        logAndWrap(e);
      }
    }
  }

  private void UpdateResultsTableFormatByUser(String userName, String format) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    Session session = currentSession();

    try {
      connection = session.connection();
      stmt = connection.prepareStatement("UPDATE  " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".USER_RESULTS_FORMAT SET ID_FRMT=? WHERE USR_NME=?");
      stmt.setString(1, format);
      stmt.setString(2, fixedCharPadding(userName, 12));
      stmt.executeUpdate();
    } catch (HibernateException e) {
      logAndWrap(e);
    } catch (SQLException e) {
      logAndWrap(e);
    } finally {
      try {
        stmt.close();
      } catch (SQLException e) {
        logAndWrap(e);
      }
    }
  }


  public ConfigurationBrowseColumn getResultsConfigurationBrowseSearch(String userName) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    ConfigurationBrowseColumn cbc = null;

    try {
      connection = currentSession().connection();
      stmt = connection.prepareStatement("SELECT * FROM  " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".USER_BROWSE_RESULTS_COLUMN WHERE USR_NME = ?");
      stmt.setString(1, fixedCharPadding(userName, 12));
      rs = stmt.executeQuery();
      while (rs.next()) {
        cbc = new ConfigurationBrowseColumn();
        cbc.setUser(rs.getString("USR_NME"));
        cbc.setAutorityColumn(rs.getString("ATRTY_TYP"));
        cbc.setLevelColumn(rs.getString("LVL_TYP"));
        cbc.setNtColumn(rs.getString("NT_TYP"));
        cbc.setRefColumn(rs.getString("REF_TYP"));
        cbc.setDocColumn(rs.getString("DOC_TYP"));
        cbc.setIndColumn(rs.getString("IND_TYP"));
        cbc.setAccColumn(rs.getString("ACC_TYP"));
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    } catch (SQLException e) {
      logAndWrap(e);
    } finally {
      try {
        rs.close();
      } catch (SQLException e) {
        logAndWrap(e);
      }
      try {
        stmt.close();
      } catch (SQLException e) {
        logAndWrap(e);
      }
    }
    return cbc;
  }


  public void saveConfigurationBrowseSearch(ConfigurationBrowseColumn bean) throws DataAccessException {
    int updated = updateConfigurationBrowseSearch(bean);
    if (updated == 0) insertConfigurationBrowseSearch(bean);
  }

  private void insertConfigurationBrowseSearch(ConfigurationBrowseColumn bean) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    Session session = currentSession();
    try {
      connection = session.connection();
      stmt = connection.prepareStatement("INSERT INTO  " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".USER_BROWSE_RESULTS_COLUMN (USR_NME,ATRTY_TYP,LVL_TYP,NT_TYP,REF_TYP,DOC_TYP,IND_TYP,ACC_TYP) VALUES (?,?,?,?,?,?,?,?)");
      stmt.setString(1, fixedCharPadding(bean.getUser(), 12));
      stmt.setString(2, bean.getAutorityColumn());
      stmt.setString(3, bean.getLevelColumn());
      stmt.setString(4, bean.getNtColumn());
      stmt.setString(5, bean.getRefColumn());
      stmt.setString(6, bean.getDocColumn());
      stmt.setString(7, bean.getIndColumn());
      stmt.setString(8, bean.getAccColumn());
      stmt.execute();
      connection.commit();
    } catch (HibernateException e) {
      e.printStackTrace();
      logAndWrap(e);
    } catch (SQLException e) {
      e.printStackTrace();
      logAndWrap(e);
    } finally {
      try {
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
        logAndWrap(e);
      }
    }
  }

  private int updateConfigurationBrowseSearch(ConfigurationBrowseColumn bean) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    Session session = currentSession();
    int updated = 0;
    try {
      connection = session.connection();
      stmt = connection.prepareStatement("UPDATE  " + System.getProperty(Global.SCHEMA_SUITE_KEY) + ".USER_BROWSE_RESULTS_COLUMN SET ATRTY_TYP=?,LVL_TYP=?,NT_TYP=?,REF_TYP=?,DOC_TYP=?,IND_TYP=?,ACC_TYP=? WHERE USR_NME=?");

      stmt.setString(1, bean.getAutorityColumn());
      stmt.setString(2, bean.getLevelColumn());
      stmt.setString(3, bean.getNtColumn());
      stmt.setString(4, bean.getRefColumn());
      stmt.setString(5, bean.getDocColumn());
      stmt.setString(6, bean.getIndColumn());
      stmt.setString(7, bean.getAccColumn());
      stmt.setString(8, fixedCharPadding(bean.getUser(), 12));
      updated = stmt.executeUpdate();
      if (updated > 0) connection.commit();
    } catch (HibernateException e) {
      logAndWrap(e);
    } catch (SQLException e) {
      logAndWrap(e);
    } finally {
      try {
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
        logAndWrap(e);
      }
    }
    return updated;
  }


}
