package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.common.HibernateUtil;

import java.util.List;

public class DAOCasSapPubl extends HibernateUtil {
  private static Log logger = LogFactory.getLog(DAOCasSapPubl.class);

  public DAOCasSapPubl() {
    super();
  }

  public List loadCasSapPubl(String editor) throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ct"
        + " from CasSapPubl as ct " + " where ct.codEditore ="
        + editor);
      q.setMaxResults(1);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  /**
   * 201107 - Il metodo ritirna la lista di tutti gli editori che hanno il codiceEditoreBreve impostato
   *
   * @param editor
   * @return
   * @throws DataAccessException
   */
  public List loadPublishersWithShortCode() throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("Select distinct ct from CasSapPubl as ct where ct.codEditoreBreve is not null");
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }
}
