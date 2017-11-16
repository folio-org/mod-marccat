package com.casalini.cataloguing.business;

import java.util.Comparator;

public class ComparatorMasterCodeAsc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 { 
//---->  Ascending sorting
	     Integer master1Code = new Integer(((MasterListElement)emp1).getIdCollection());        
	     Integer master2Code = new Integer(((MasterListElement)emp2).getIdCollection());
	     return master1Code.compareTo(master2Code);
	 }
}
