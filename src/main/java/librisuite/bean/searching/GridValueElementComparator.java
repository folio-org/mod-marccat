package librisuite.bean.searching;

import java.util.Comparator;
import java.util.List;

import librisuite.business.codetable.ValueLabelElement;

public class GridValueElementComparator implements Comparator {
	
	private int column;

	public GridValueElementComparator(int column) {
		this.column = column;
	}
	
	public int compare(Object c1, Object c2) {
		ValueLabelElement v1 = (ValueLabelElement) ((List)c1).get(column);
		ValueLabelElement v2 = (ValueLabelElement) ((List)c2).get(column);
		return v1.compareTo(v2);
	}

}
