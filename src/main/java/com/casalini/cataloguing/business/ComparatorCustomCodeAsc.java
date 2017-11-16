package com.casalini.cataloguing.business;

import java.util.Comparator;

public class ComparatorCustomCodeAsc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 { 
//---->  Ascending sorting
	     Integer custom1Code = new Integer(((CustomListElement)emp1).getIdCollection());        
	     Integer custom2Code = new Integer(((CustomListElement)emp2).getIdCollection());
	     return custom1Code.compareTo(custom2Code);
	 }
}
