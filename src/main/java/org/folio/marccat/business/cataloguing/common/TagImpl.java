
package org.folio.marccat.business.cataloguing.common;

import net.sf.hibernate.Session;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.shared.Validation;
import java.io.Serializable;


/**
 * This class is the Abstract Implementation of the Tag interface
 * Subclasses are intended to be different schemes/formats of tags
 * (e.g. MARC21 BIB, MARC21 AUT)
 * <p>
 * An instance of this class is composed within the Tag class so that
 * format specific differences can be bridged through the common Tag interface
 */
public abstract class TagImpl implements Serializable {

  public abstract String getHeadingType(Tag t);

  public abstract Catalog getCatalog();

   /**
   * @return the MARC tag and indicators for this tag
   */
  public abstract CorrelationKey getMarcEncoding(final Tag t, final Session session) throws DataAccessException;

  public abstract Validation getValidation(final Tag t, final Session session) throws DataAccessException;

  public abstract Correlation getCorrelation(String tagNumber, char indicator1, char indicator2, int category, Session session) throws DataAccessException;
}
