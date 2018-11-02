package org.folio.cataloging.business.common.filter;

import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;

public interface TagFilter {
  boolean accept(Tag tag, Object optionalCondition) throws DataAccessException;
}
