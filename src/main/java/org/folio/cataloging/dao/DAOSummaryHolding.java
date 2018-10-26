package org.folio.cataloging.dao;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.dao.persistence.CPY_ID;
import org.folio.cataloging.dao.persistence.SMRY_HLDG;

import java.util.List;

public class DAOSummaryHolding extends HibernateUtil {
  public void createSummaryHoldingIfRequired(final Session session, final CPY_ID copy) throws DataAccessException {
    SMRY_HLDG aHldg = new SMRY_HLDG(copy);
    if (get(session, SMRY_HLDG.class, aHldg) == null) {
      // SMRY_HLDG does not yet exist so save the default values
      persistByStatus(aHldg);
    }
  }

  public int countCopies(int amicusNumber, int orgNumber) throws DataAccessException {
    List l = find("select count(*) from CPY_ID as c where c.organisationNumber = ?"
      + " and c.bibItemNumber = ?", new Object[]{
      new Integer(orgNumber), new Integer(amicusNumber)}, new Type[]{
      Hibernate.INTEGER, Hibernate.INTEGER});

    if (l.size() > 0) {
      return ((Integer) l.get(0)).intValue();
    } else {
      return 0;
    }
  }

  public void deleteRecord(final int amicusNumber, final int orgNumber) throws DataAccessException {
    Session s = currentSession();
    try {

      if (countCopies(amicusNumber, orgNumber) == 1) {
        s.delete(
          "from SMRY_HLDG as c where c.mainLibraryNumber = ?"
            + " and c.bibItemNumber = ?", new Object[]{
            new Integer(orgNumber), new Integer(amicusNumber)},
          new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    }
  }

  public SMRY_HLDG getSmryHoldingByAmicusNumberOrgNbr(int amicusNumber, int orgNumber) throws DataAccessException {
    List l = find("from SMRY_HLDG as c"
        + " where c.mainLibraryNumber = ?"
        + " and c.bibItemNumber = ?",
      new Object[]{new Integer(orgNumber), new Integer(amicusNumber)},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    if (l.size() == 1) {
      return (SMRY_HLDG) l.get(0);
    } else {
      return null;
    }
  }

  public void deleteSummaryHolding(final int amicusNumber, final int orgNumber) throws DataAccessException {
    Session s = currentSession();
    try {
      s.delete("from SMRY_HLDG as c where c.mainLibraryNumber = ?"
          + " and c.bibItemNumber = ?", new Object[]{
          new Integer(orgNumber), new Integer(amicusNumber)},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
    } catch (HibernateException e) {
      logAndWrap(e);
    }
  }
}
