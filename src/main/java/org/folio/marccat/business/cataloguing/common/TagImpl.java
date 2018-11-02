/*
 * (c) LibriCore
 *
 * Created on Nov 4, 2005
 *
 * TagImpl.java
 */
package org.folio.marccat.business.cataloguing.common;

import net.sf.hibernate.Session;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.persistence.Correlation;
import org.folio.marccat.dao.persistence.CorrelationKey;
import org.folio.marccat.shared.Validation;

import java.io.Serializable;
import java.util.Set;

/**
 * This class is the Abstract Implementation of the Tag interface
 * Subclasses are intended to be different schemes/formats of tags
 * (e.g. MARC21 BIB, MARC21 AUT)
 * <p>
 * An instance of this class is composed within the Tag class so that
 * format specific differences can be bridged through the common Tag interface
 *
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2005/12/12 12:54:36 $
 * @since 1.0
 */
public abstract class TagImpl implements Serializable {

  abstract public String getHeadingType(Tag t);

  abstract public Catalog getCatalog();

  /**
   * @return the MARC tag and indicators for this tag
   */
  @Deprecated
  abstract public CorrelationKey getMarcEncoding(final Tag t) throws DataAccessException;

  /**
   * @return the MARC tag and indicators for this tag
   */
  abstract public CorrelationKey getMarcEncoding(final Tag t, final Session session) throws DataAccessException;

  @Deprecated
  abstract public Validation getValidation(Tag t) throws DataAccessException;

  abstract public Validation getValidation(final Tag t, final Session session) throws DataAccessException;

  /**
   * return the list of subfields that can be edited on the worksheet
   *
   * @since 1.0
   */
  abstract public Set getValidEditableSubfields(int category);

  abstract public Correlation getCorrelation(String tagNumber, char indicator1, char indicator2, int category, Session session) throws DataAccessException;
}
