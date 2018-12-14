package org.folio.marccat.business.common.filter;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.exception.DataAccessException;

public class NoTagFilter implements TagFilter {

  public boolean accept(Tag tag, Object optionalCondition) throws DataAccessException {
    return false;
  }

}
