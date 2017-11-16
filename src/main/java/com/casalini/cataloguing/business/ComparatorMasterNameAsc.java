package com.casalini.cataloguing.business;

import java.util.Comparator;

public class ComparatorMasterNameAsc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 {   
//---->  Ascending sorting  	 	       
	     String master1Name = ((MasterListElement)emp1).getNameIta();        
	     String master2Name = ((MasterListElement)emp2).getNameIta();
	     return master1Name.toUpperCase().compareTo(master2Name.toUpperCase());
	 }
}
