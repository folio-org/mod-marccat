package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.S_SYS_GLBL_VRBL;
import org.folio.marccat.exception.DataAccessException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Provides access to S_SYS_GLBL_VRBL
 *
 * @author paulm
 * @version %I%, %Global%
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public class DAOGlobalVariable extends HibernateUtil implements Serializable {

  private static Log logger = LogFactory.getLog(DAOGlobalVariable.class);


  @Deprecated
  public String getValueByName(final String name) {
    throw new IllegalArgumentException("don't call me!");
  }

  public String getValueByName(final String name, final Session session) {
    String valueByName = null;
    S_SYS_GLBL_VRBL ss = ((S_SYS_GLBL_VRBL) get(session, S_SYS_GLBL_VRBL.class, name));
    if (ss != null) {
      valueByName = ss.getValue();
    }
    return valueByName;
  }

  public void edit(final S_SYS_GLBL_VRBL globalVrbl) {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s) throws HibernateException {
        s.update(globalVrbl);
      }
    }
      .execute();
  }

  public void setValueByName(final Session session, final String name, final String value) {
    S_SYS_GLBL_VRBL sysGlobal = (S_SYS_GLBL_VRBL) get(session, S_SYS_GLBL_VRBL.class, name);
    sysGlobal.setValue(value);
    edit(sysGlobal);
  }

  public HashMap<String, String> getAllGlobalVariable(final Session session) {
    List<S_SYS_GLBL_VRBL> listAllKeys = null;
    HashMap<String, String> hash = new HashMap<>();
    try {
      listAllKeys = find(session, "from S_SYS_GLBL_VRBL");
    } catch (DataAccessException e) {
      logger.error(e);
    }
    Iterator<S_SYS_GLBL_VRBL> iter = listAllKeys.iterator();
    while (iter.hasNext()) {
      S_SYS_GLBL_VRBL rawGlobalVar = iter.next();
      hash.put(rawGlobalVar.getName(), rawGlobalVar.getValue());
    }
    return hash;
  }
}
