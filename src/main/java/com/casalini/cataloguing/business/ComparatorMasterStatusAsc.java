package com.casalini.cataloguing.business;

import java.util.Comparator;

public class ComparatorMasterStatusAsc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 {    	 	       
//-----> Ascending sorting
	     String master1Status = ((MasterListElement)emp1).getStatusCode();        
	     String master2Status = ((MasterListElement)emp2).getStatusCode();
	     return master1Status.toUpperCase().compareTo(master2Status.toUpperCase());
	 }
}
