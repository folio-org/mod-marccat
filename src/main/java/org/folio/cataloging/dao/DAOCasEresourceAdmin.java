package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.RecordNotFoundException;

import java.util.List;

public class DAOCasEresourceAdmin extends AbstractDAO {
  private static Log logger = LogFactory.getLog (DAOCasEresourceAdmin.class);

  public DAOCasEresourceAdmin() {
    super ( );
  }

  public List loadCasEresourceAdmin(int bibNumber) throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession ( );
      Query q = s.createQuery ("select distinct ct"
        + " from CasEresourceAdmin as ct " + " where ct.bibItemNumber ="
        + bibNumber);
      q.setMaxResults (1);
      result = q.list ( );

    } catch (HibernateException e) {
      logAndWrap (e);
    }
    return result;
  }

  public List loadCasSapPubl(String editor) throws DataAccessException {
    String editore = editor.toLowerCase ( );
    List result = null;
    try {
      Session s = currentSession ( );
      result = s.find ("from CasSapPubl as c " + " where lower(c.codEditore) = ? ",
        new Object[]{editore}, new Type[]{Hibernate.STRING});

      if (result.size ( ) == 0) {
        throw new RecordNotFoundException ("CasSapPubl : codice editore " + editor + " not found");
      }

    } catch (HibernateException e) {
      logAndWrap (e);
    }
    return result;
  }

  public List loadCasSapPublBreve(String editorSmall) throws DataAccessException {
    String editorBreve = editorSmall.toLowerCase ( );
    List result = null;
    try {
      Session s = currentSession ( );
      result = s.find ("from CasSapPubl as c " + " where lower(c.codEditoreBreve) = ? ",
        new Object[]{editorBreve}, new Type[]{Hibernate.STRING});

      if (result.size ( ) == 0) {
        throw new RecordNotFoundException ("CasSapPubl : codice editore breve" + editorBreve + " not found");
      }

    } catch (HibernateException e) {
      logAndWrap (e);
    }
    return result;
  }
}
