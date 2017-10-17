package librisuite.business.common.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import librisuite.business.cataloguing.common.Tag;

public class MultiTagContainer implements TagContainer {
	private List tags = new ArrayList();
	
	public void add(Tag tag) {
		tags.add(tag);
	}

	public Tag getLeaderTag() {
		if(tags.isEmpty()) return null;
		return (Tag) tags.get(0);
	}

	public Iterator iterator() {
		return tags.iterator();
	}

	public int compareTo(Object arg0) {
		try {
			TagContainer tc0 = (TagContainer) arg0;
			return this.getLeaderTag().getMarcEncoding().getMarcTag()
			.compareTo(tc0.getLeaderTag().getMarcEncoding().getMarcTag());
		} catch (Exception e) {
			return 0;
		} 
	}

	public Collection getList() {
		return tags;
	}

	public void sort() {
		Collections.sort(tags, new SequenceNumberComparator());
	}

}
