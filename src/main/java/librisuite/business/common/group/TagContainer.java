package librisuite.business.common.group;

import java.util.Collection;
import java.util.Iterator;

import librisuite.business.cataloguing.common.Tag;

public interface TagContainer extends Comparable {
	Tag getLeaderTag();
	void add(Tag tag);
	Iterator iterator();
	Collection getList();
	void sort();
}
