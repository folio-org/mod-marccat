package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.persistence.CasPublCdeIsbn;

import java.util.List;

public class DAOPublCdeIsbn extends HibernateUtil {

  public DAOPublCdeIsbn() {
    super();
  }

  public List loadIsbnFromEditor(String codEditore) throws DataAccessException {
    List result = null;
    try {

      Session s = currentSession();
      result = s.find("from CasPublCdeIsbn as a where a.codEditore=" + "'" + codEditore + "'" + "order by a.isbnSortForm");

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadIsbnFromEditorIsbn(CasPublCdeIsbn item) throws DataAccessException {
    List result = null;
    try {

      Session s = currentSession();
      result = s.find("from CasPublCdeIsbn as a where a.isbnSortForm ="
        + "'" + item.getIsbnSortForm() + "'"
        + " and a.codEditore ="
        + "'" + item.getCodEditore() + "'");

//			if (result.size()>0){
//				throw new DuplicateKeyException();
//			}

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadAssociatedEditorFromIsbn(CasPublCdeIsbn item) throws DataAccessException {
    List result = null;
    try {

      Session s = currentSession();
      result = s.find("from CasPublCdeIsbn as a where a.isbnSortForm ="
        + "'" + item.getIsbnSortForm() + "'"
        + " and not a.codEditore ="
        + "'" + item.getCodEditore() + "'");

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadEditorFromIsbn(String isbn) throws DataAccessException {
    List result = null;
    try {

      Session s = currentSession();
      result = s.find("from CasPublCdeIsbn as a where a.isbnSortForm =" + "'" + isbn + "'");

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  public List loadEditorFromIsbn2(String isbn) throws DataAccessException {
//		System.out.println("Isbn per select : " + isbn);

    List result = null;
    Query q = null;
    try {
      Session s = currentSession();
      q = s.createQuery("select due"
        + " from CasPublCdeIsbn as uno, "
        + " CasSapPubl as due "
        + " where uno.isbnSortForm = '" + isbn + "'"
        + " and uno.codEditore = due.codEditore "
        + " order by uno.codEditore");
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }
}
