package com.casalini.cataloguing.business;

import java.util.Comparator;

public class ComparatorCustomCodeDesc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 { 
//---->  Descending sorting
	     Integer custom1Code = new Integer(((CustomListElement)emp1).getIdCollection());        
	     Integer custom2Code = new Integer(((CustomListElement)emp2).getIdCollection());
	     return custom2Code.compareTo(custom1Code);
	 }
}
