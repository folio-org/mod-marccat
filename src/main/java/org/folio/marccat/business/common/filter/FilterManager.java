package org.folio.marccat.business.common.filter;

import org.folio.marccat.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.marccat.business.cataloguing.common.Tag;
import org.folio.marccat.business.common.DataAccessException;

public interface FilterManager {
  TagFilter getFilter(Tag tag) throws MarcCorrelationException, DataAccessException;
}
