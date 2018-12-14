package org.folio.marccat.business.common.filter;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.exception.DataAccessException;

public interface FilterManager {
  TagFilter getFilter(Tag tag) throws DataAccessException;
}
