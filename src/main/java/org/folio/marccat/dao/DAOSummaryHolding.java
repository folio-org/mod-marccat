package org.folio.marccat.dao;

import java.util.List;

import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.persistence.CPY_ID;
import org.folio.marccat.dao.persistence.SMRY_HLDG;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.Type;

public class DAOSummaryHolding extends HibernateUtil {
	private String queryAndBibNumber = " and c.bibItemNumber = ?";
  public void createSummaryHoldingIfRequired(final Session session, final CPY_ID copy)  {
    SMRY_HLDG aHldg = new SMRY_HLDG(copy);
  }

  public int countCopies(int amicusNumber, int orgNumber)  {
    List l = find("select count(*) from CPY_ID as c where c.organisationNumber = ?"
      + queryAndBibNumber, new Object[]{
      (orgNumber), (amicusNumber)}, new Type[]{
      Hibernate.INTEGER, Hibernate.INTEGER});

    if (!l.isEmpty()) {
      return ((Integer) l.get(0)).intValue();
    } else {
      return 0;
    }
  }

  public void deleteRecord(final int amicusNumber, final int orgNumber)  {
    Session s = currentSession();
    try {

      if (countCopies(amicusNumber, orgNumber) == 1) {
        s.delete(
          "from SMRY_HLDG as c where c.mainLibraryNumber = ?"
            + queryAndBibNumber, new Object[]{
            (orgNumber), (amicusNumber)},
          new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
      }
    } catch (HibernateException e) {
      logAndWrap(e);
    }
  }

  public SMRY_HLDG getSmryHoldingByAmicusNumberOrgNbr(int amicusNumber, int orgNumber)  {
    List l = find("from SMRY_HLDG as c"
        + " where c.mainLibraryNumber = ?"
        + queryAndBibNumber,
      new Object[]{(orgNumber), (amicusNumber)},
      new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});

    if (l.size() == 1) {
      return (SMRY_HLDG) l.get(0);
    } else {
      return null;
    }
  }

  public void deleteSummaryHolding(final int amicusNumber, final int orgNumber)  {
    Session s = currentSession();
    try {
      s.delete("from SMRY_HLDG as c where c.mainLibraryNumber = ?"
          + queryAndBibNumber, new Object[]{
          (orgNumber), (amicusNumber)},
        new Type[]{Hibernate.INTEGER, Hibernate.INTEGER});
    } catch (HibernateException e) {
      logAndWrap(e);
    }
  }
}
