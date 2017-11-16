package librisuite.business.common.filter;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;

public interface FilterManager {
	TagFilter getFilter(Tag tag) throws MarcCorrelationException, DataAccessException;
}
