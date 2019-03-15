package org.folio.marccat.business.common.filter;

import org.folio.marccat.business.cataloguing.common.Tag;

public interface FilterManager {
  TagFilter getFilter(Tag tag);
}
