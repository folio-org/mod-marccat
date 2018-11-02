package org.folio.marccat.business.common.filter;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.DataAccessException;

public interface TagFilter {
  boolean accept(Tag tag, Object optionalCondition) throws DataAccessException;
}
