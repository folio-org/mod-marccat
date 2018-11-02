package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.codetable.Avp;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.persistence.CodeTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * pm 2011
 * Provides specific DAOCodeTable behaviour for DB_LIST.
 * Only AMICUS bibliographic views are selected.
 *
 * @author pmouland
 * @since 1.0
 */
public class DAODatabaseViewList extends DAOCodeTable {
  private static Log logger = LogFactory.getLog(DAODatabaseViewList.class);

  /* (non-Javadoc)
   * @see DAOCodeTable#getList(java.lang.Class, java.util.Locale)
   */
  public List <Object> getList(Class c, Locale locale) throws DataAccessException {
    List listCodeTable = null;

    logger.debug("getList(" + c.getName() + ", " + locale.getDisplayName() + ")");
    try {
      Session s = currentSession();
      listCodeTable =
        s.find(
          "from "
            + c.getName()
            + " as ct "
            + " where ct.language = ?"
            + " and ct.obsoleteIndicator = '0'"
            + " and ct.system = 0"
            + " and ct.code >= -1"
            + " order by ct.code ",
          new Object[]{locale.getISO3Language()},
          new Type[]{Hibernate.STRING});
    } catch (HibernateException e) {
      logAndWrap(e);
    }
    logger.debug("Got codetable for " + c.getName());
    List result = new ArrayList();
    Iterator iterator = listCodeTable.iterator();

    while (iterator.hasNext()) {
      CodeTable element = (CodeTable) iterator.next();
      if (element.getLanguage().equals(locale.getISO3Language())) {
        result.add(new Avp(element.getCodeString().trim(), element.getLongText()));
      }
    }
    return result;
  }

}
