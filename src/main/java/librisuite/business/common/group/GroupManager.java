package librisuite.business.common.group;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;

public interface GroupManager {
	TagGroup getGroup(Tag tag) throws MarcCorrelationException, DataAccessException;
	boolean isSameGroup(Tag tag1, Tag tag2) throws MarcCorrelationException, DataAccessException;
	void add(TagGroup group);
}
