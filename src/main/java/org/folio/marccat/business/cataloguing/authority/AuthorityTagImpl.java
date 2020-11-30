package org.folio.marccat.business.cataloguing.authority;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.cataloguing.bibliographic.PersistsViaItem;
import org.folio.marccat.business.cataloguing.common.Catalog;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.cataloguing.common.TagImpl;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.dao.AuthorityCorrelationDAO;
import org.folio.marccat.dao.persistence.AUT;
import org.folio.marccat.dao.persistence.AuthorityCorrelation;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.resources.domain.Heading;
import org.folio.marccat.shared.Validation;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * @author elena
 *
 */
public class AuthorityTagImpl extends TagImpl {

  private static final long serialVersionUID = -1006497560481032279L;

  private static final Log logger = LogFactory.getLog(AuthorityTagImpl.class);

  private static final AuthorityCorrelationDAO daoCorrelation = new AuthorityCorrelationDAO();

  public AuthorityTagImpl() {
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
      logger.warn("Corr_1: " + t.getCorrelation(1) + ", Corr_2: " + t.getCorrelation(2) + ", Corr_3: "
          + t.getCorrelation(3) + ", Category: " + t.getCategory());
    }
    return key;
  }

  public String getHeadingType(Tag t) {
    return ((AUT) ((PersistsViaItem) t).getItemEntity()).getHeadingType();
  }

  public Catalog getCatalog() {
    return new AuthorityCatalog();
  }

  public Correlation getCorrelation(String tagNumber, char indicator1, char indicator2, final int category, final Session session) {
    try {
      return daoCorrelation.getAuthorityCorrelation(session, tagNumber, indicator1, indicator2, category);
    } catch (HibernateException e) {
      throw new DataAccessException(e);
    }
  }

  /**
   * @deprecated the correlation key is not used
   */

  @Deprecated
  public CorrelationKey getMarcEncoding(Tag t) {
    return null;
  }

  /**
   * @deprecated the tag validation is not used
   */

  @Deprecated
  public Validation getValidation(Tag t) {
    return null;
  }

  @Override
  public Validation getValidation(Tag t, Session session) {
    // TODO It is an abstract class that should be implemented. At the moment this
    // function is not used.
    return null;
  }

  public int getTagCategory(final Heading heading, Session session) {

    try {
      List<AuthorityCorrelation> correlations = daoCorrelation.getCategoryCorrelation(session, heading.getTag(),
          (heading.getInd1() != null ? heading.getInd1().charAt(0) : ' '),
          heading.getInd2() != null ? heading.getInd2().charAt(0) : ' ');
      if (correlations.stream().anyMatch(Objects::nonNull) && correlations.size() == 1) {
        Optional<AuthorityCorrelation> firstElement = correlations.stream().findFirst();
        if (firstElement.isPresent())
          return firstElement.get().getKey().getMarcTagCategoryCode();
      } else {
        if ((correlations.size() > 1) && (correlations.stream().anyMatch(Objects::nonNull))) {
          Optional<AuthorityCorrelation> firstElement = correlations.stream().findFirst();
          if (firstElement.isPresent())
            return firstElement.get().getKey().getMarcTagCategoryCode();
        }
      }
      return 0;

    } catch (final HibernateException exception) {
      logger.error(Message.MOD_MARCCAT_00010_DATA_ACCESS_FAILURE, exception);
      throw new DataAccessException(exception);
    }
  }
}
