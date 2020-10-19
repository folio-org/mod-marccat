package org.folio.marccat.business.cataloguing.bibliographic;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.common.Catalog;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.dao.BibliographicCorrelationDAO;
import org.folio.marccat.dao.BibliographicValidationDAO;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.Validation;


public class BibliographicTagImpl extends TagImpl {
  private static final Log logger = LogFactory.getLog(BibliographicTagImpl.class);
  private static final BibliographicValidationDAO daoValidation = new BibliographicValidationDAO();
  private static final BibliographicCorrelationDAO daoCorrelation = new BibliographicCorrelationDAO();

  /**
   *
   */
  public BibliographicTagImpl() {
    super();
  }

  /**
   * @return the MARC tag and indicators for this tag
   */
  public CorrelationKey getMarcEncoding(final Tag t, final Session session) {
    CorrelationKey key = null;
    try {
      key = daoCorrelation.getMarcEncoding(t.getCategory(), t.getCorrelation(1), t.getCorrelation(2), t.getCorrelation(3), session);
    } catch (HibernateException e) {
      throw new DataAccessException();
    }

    if (key == null) {
      logger.warn("MarcCorrelationException in getMarcEncoding for values: ");
      logger.warn("Corr_1: " + t.getCorrelation(1) + ", Corr_2: "
        + t.getCorrelation(2) + ", Corr_3: " + t.getCorrelation(3)
        + ", Category: " + t.getCategory());
    }
    return key;
  }

  /*
   * (non-Javadoc)
   *
   * @see TagImpl#getHeadingType(Tag)
   */
  public String getHeadingType(Tag t) {
    return null;
  }

  public Validation getValidation(Tag t, final Session session) {
    try {
      return daoValidation.load(session, t.getCategory(), t.getCorrelationValues());
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }


   /*
   * (non-Javadoc)
   *
   * @see TagImpl#getCatalog()
   */
  public Catalog getCatalog() {
    return new BibliographicCatalog();
  }



  public Correlation getCorrelation(String tagNumber, char indicator1, char indicator2, final int category, final Session session) {
    try {
      return daoCorrelation.getBibliographicCorrelation(session, tagNumber, indicator1, indicator2, category);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

}
