package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.common.TransactionalHibernateOperation;
import org.folio.marccat.dao.persistence.T_CAS_STND_NTE_SUB_TYP;

import java.util.List;

public class DAONoteStandardSubdivision extends HibernateUtil {

  public void saveNoteStandardSubdivision(String code, String optCodeNote,
                                          int i) throws DataAccessException {

    DAOCodeTable daoCodeTable = new DAOCodeTable();
    T_CAS_STND_NTE_SUB_TYP sub;
    if (i == 0) {
      List codeNoteList = daoCodeTable.getCodeNoteList(new Integer(code).intValue());
      if (codeNoteList.size() == 0) {
        sub = new T_CAS_STND_NTE_SUB_TYP();
        sub.setSequence(new Integer(code).intValue());
        sub.setCode(new Integer(optCodeNote).intValue());
      } else {
        sub = (T_CAS_STND_NTE_SUB_TYP) codeNoteList.get(0);
        sub.setCode(new Integer(optCodeNote).intValue());
        sub.markChanged();
      }
      saveSubTyp(sub);
    }
  }

  public void saveSubTyp(final T_CAS_STND_NTE_SUB_TYP sub) throws DataAccessException {
    new TransactionalHibernateOperation() {
      public void doInHibernateTransaction(Session s)
        throws HibernateException, DataAccessException {
        persistByStatus(sub);
      }

    }.execute();
  }

}
