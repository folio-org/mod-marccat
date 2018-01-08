package org.folio.cataloging.bean.searching;

import org.folio.cataloging.business.codetable.Avp;

import java.util.Comparator;
import java.util.List;

public class GridValueElementComparator implements Comparator {
	
	private int column;

	public GridValueElementComparator(int column) {
		this.column = column;
	}
	
	public int compare(Object c1, Object c2) {
		Avp v1 = (Avp) ((List)c1).get(column);
		Avp v2 = (Avp) ((List)c2).get(column);
		return v1.compareTo(v2);
	}

}
