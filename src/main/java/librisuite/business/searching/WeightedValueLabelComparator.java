package librisuite.business.searching;
import java.util.Comparator;

import librisuite.business.codetable.ValueLabelElement;


public class WeightedValueLabelComparator implements Comparator {

	public int compare(Object arg0, Object arg1) {
		String s0 = ((ValueLabelElement) arg0).getLabel();
		String s1 = ((ValueLabelElement) arg1).getLabel();
		if(s0.length()>s1.length()) return -1;
		if(s1.length()>s0.length()) return 1;
		if(s0.indexOf(s1)>=0) return -1;
		if(s1.indexOf(s0)>=0) return 1;
		return 0;
	}

}
