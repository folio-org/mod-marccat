package org.folio.marccat.dao;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.persistence.CasFiles;
import org.folio.marccat.dao.persistence.ControlNumberAccessPoint;

import java.util.List;

public class DAOCasFiles extends AbstractDAO {
  private static final Log logger = LogFactory.getLog(DAOCasFiles.class);

  public DAOCasFiles() {
    super();
  }

  public void persistCasFiles(CasFiles casFiles, ControlNumberAccessPoint tag097) throws DataAccessException {
    CasFiles casFiles2;
    List result = loadCasFilesByKey(tag097.getStringText().getSubfieldsWithCodes("c").getDisplayText().toString().trim(), tag097.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim());
    if (result.size() == 0) {
      casFiles.markNew();
      persistByStatus(casFiles);
    } else {
      casFiles2 = (CasFiles) result.get(0);
      casFiles2.markChanged();
      persistByStatus(casFiles2);
    }
  }

  /**
   * Metodo che legge la tabella CasFiles per chiave
   */
  public List loadCasFilesByKey(String bibItemNumberFigliaString, String bibItemNumberMadreString) throws DataAccessException {
    int bibItemNumberMadre = 0;
    int bibItemNumberFiglia = 0;

    if (bibItemNumberFigliaString.length() > 0)
      bibItemNumberFiglia = Integer.parseInt(bibItemNumberFigliaString);

    if (bibItemNumberMadreString.length() > 0)
      bibItemNumberMadre = Integer.parseInt(bibItemNumberMadreString);

    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ct" +
        " from CasFiles as ct " +
        " where ct.bibItemNumberFiglia =" + bibItemNumberFiglia +
        " and ct.bibItemNumberMadre = " + bibItemNumberMadre
      );
      q.setMaxResults(1);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

  /**
   * Metodo che legge la CasFiles per chiave e poi la cancella
   *
   * @param tag097
   * @throws DataAccessException
   */
  public void deleteCasFiles(ControlNumberAccessPoint tag097) throws DataAccessException {
    int bibNumber = 0;
    int bibNumberMadre = 0;
    StringBuffer buffer = new StringBuffer();
    CasFiles casFiles;

    List result = loadCasFilesByKey(tag097.getStringText().getSubfieldsWithCodes("c").getDisplayText().toString().trim(), tag097.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim());
    if (result.size() == 0) {
      bibNumber = Integer.parseInt(tag097.getStringText().getSubfieldsWithCodes("c").getDisplayText());
      if (tag097.getStringText().getSubfieldsWithCodes("a").getDisplayText().toString().trim().length() > 0)
        bibNumberMadre = Integer.parseInt(tag097.getStringText().getSubfieldsWithCodes("a").getDisplayText());
      buffer.append("ATTENZIONE --> In DAOCasFiles, metodo deleteCasFiles - Cancellazione impossibile record non trovato per AN_FIGLIA: ")
        .append(bibNumber)
        .append(" e AN_MADRE: ")
        .append(bibNumberMadre);
      logger.error(buffer.toString());
    } else {
      casFiles = (CasFiles) result.get(0);
      casFiles.markDeleted();
      casFiles.getDAO().persistByStatus(casFiles);
    }
  }

  /**
   * 20101018
   * Metodo che legge la tabella CasFiles per bibItemMadre
   */
  public List loadCasFilesByBibItemMadre(int bibItemMadre) throws DataAccessException {
    List result = null;
    try {
      Session s = currentSession();
      Query q = s.createQuery("select distinct ct from CasFiles as ct where ct.bibItemNumberMadre = " + bibItemMadre);
      result = q.list();

    } catch (HibernateException e) {
      logAndWrap(e);
    }
    return result;
  }

}
