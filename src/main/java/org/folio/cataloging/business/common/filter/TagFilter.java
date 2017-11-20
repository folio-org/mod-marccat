package org.folio.cataloging.business.common.filter;

import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;

public interface TagFilter {

	public boolean accept(Tag tag, Object optionalCondition) 
		throws MarcCorrelationException, DataAccessException;
}
