package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.View;
import org.folio.cataloging.dao.persistence.HDG_URI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class DAOHeadingUri extends AbstractDAO {
  final static String SELECT_URI_BY_ALL_VIEW = "SELECT COUNT(*) FROM HDG_URI AS HDG WHERE HDG.headingNumber = ? AND HDG.headingTypeCode = ? AND HDG.sourceId = ? AND HDG.uri = ?";
  final static String SELECT_URI_BY_ONE_VIEW = "SELECT COUNT(*) FROM HDG_URI AS HDG WHERE HDG.headingNumber = ? AND HDG.headingTypeCode = ? AND HDG.sourceId = ? AND HDG.uri = ? AND SUBSTR(HDG.userView, ?, 1) = '1'";
  final static String SELECT_SOURCE_LIST_BY_HDG_CATEGORY =
    "SELECT TBL_VLU_CDE, STRING_TEXT FROM OLISUITE.T_SRC_URI_TYP WHERE LANGID = ? AND TBL_URI_CAT_CDE = ? AND TBL_VLU_OBSLT_IND = '0' ORDER BY STRING_TEXT";
  private Log logger = LogFactory.getLog(DAOHeadingUri.class);

  /* Bug 5424 */
  public boolean isPresentURI(HDG_URI headingUri, int searchingView) throws DataAccessException {
    int result = 0;
    List l = null;

    if (searchingView == View.ANY) {
      l = find(SELECT_URI_BY_ALL_VIEW,
        new Object[]{new Integer(headingUri.getHeadingNumber()), headingUri.getHeadingTypeCode(), headingUri.getSourceId(), headingUri.getUri()},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.STRING});
    } else {
      l = find(SELECT_URI_BY_ONE_VIEW,
        new Object[]{new Integer(headingUri.getHeadingNumber()), headingUri.getHeadingTypeCode(), headingUri.getSourceId(), headingUri.getUri(), new Integer(searchingView)},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.INTEGER, Hibernate.STRING, Hibernate.INTEGER});
    }
    if (l.size() > 0) {
      result = ((Integer) l.get(0)).intValue();
    }
    return (result > 0);
  }

  public List <Avp> loadSourceUriListByHdgCategory(Locale locale, int hdgCategory) throws DataAccessException {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List <Avp> list = new ArrayList <Avp>();

    try {
      connection = currentSession().connection();
      stmt = connection.prepareStatement(SELECT_SOURCE_LIST_BY_HDG_CATEGORY);
      stmt.setString(1, locale.getISO3Language());
      stmt.setInt(2, hdgCategory);
      rs = stmt.executeQuery();

      while (rs.next()) {
        Avp ve = new Avp(rs.getString("TBL_VLU_CDE"), rs.getString("STRING_TEXT"));
        list.add(ve);
      }

    } catch (HibernateException e) {
      logAndWrap(e);
    } catch (SQLException e) {
      logAndWrap(e);
    } finally {
      try {
        rs.close();
      } catch (Exception ex) {
      }
      try {
        stmt.close();
      } catch (Exception ex) {
      }
    }
    return list;
  }
}
