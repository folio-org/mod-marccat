package org.folio.marccat.business.common.group;

import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.exception.DataAccessException;

public interface TagGroup {
  boolean contains(Tag tag) throws DataAccessException;

  boolean isCanSort();

  boolean isSingleSort();
}
