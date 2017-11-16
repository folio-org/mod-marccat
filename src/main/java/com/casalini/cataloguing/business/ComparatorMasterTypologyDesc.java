package com.casalini.cataloguing.business;

import java.util.Comparator;

public class ComparatorMasterTypologyDesc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 {    	 	       
//-----> Descending sorting
	     String master1Tipology = ((MasterListElement)emp1).getTypologyCode();        
	     String master2Tipology = ((MasterListElement)emp2).getTypologyCode();
	     return master2Tipology.toUpperCase().compareTo(master1Tipology.toUpperCase());
	 }
}
