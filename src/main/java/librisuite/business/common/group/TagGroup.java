package librisuite.business.common.group;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;

public interface TagGroup {
	boolean contains(Tag tag) throws MarcCorrelationException, DataAccessException;
	boolean isCanSort();
	boolean isSingleSort();
}
