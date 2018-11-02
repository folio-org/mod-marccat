package org.folio.cataloging.business.common.filter;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;

public interface FilterManager {
  TagFilter getFilter(Tag tag) throws MarcCorrelationException, DataAccessException;
}
