package librisuite.business.marchelper;

import java.util.Comparator;

import librisuite.hibernate.TAG_MODEL;

public class TagModelComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		TAG_MODEL tm1 = (TAG_MODEL) o1;
		TAG_MODEL tm2 = (TAG_MODEL) o2;
		int s1 = tm1.getComplexity();
		int s2 = tm2.getComplexity();
		if(s1 == s2) return 0;
		if(s1<s2) return 1;
		return -1;
	}

}
