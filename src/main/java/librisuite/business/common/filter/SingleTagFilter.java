package librisuite.business.common.filter;

import librisuite.business.cataloguing.bibliographic.MarcCorrelationException;
import librisuite.business.cataloguing.common.Tag;
import librisuite.business.common.DataAccessException;

public class SingleTagFilter implements TagFilter {
	private Tag sourceTag;
	
	public SingleTagFilter(Tag sourceTag) {
		super();
		this.sourceTag = sourceTag;
	}

	public boolean accept(Tag tag, Object optionalCondition) throws MarcCorrelationException, DataAccessException {
		return 
			sourceTag.getMarcEncoding().getMarcTag()
			.equals(tag.getMarcEncoding().getMarcTag());
	}
}
