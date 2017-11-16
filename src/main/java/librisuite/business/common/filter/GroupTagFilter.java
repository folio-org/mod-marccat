package librisuite.business.common.filter;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.group.TagGroup;

public class GroupTagFilter implements TagFilter {
	private TagGroup group;
	
	public GroupTagFilter(TagGroup group) {
		super();
		this.group = group;
	}
	
	public boolean accept(Tag tag, Object optionalCondition) throws MarcCorrelationException, DataAccessException {
		return group.contains(tag);
	}

}
