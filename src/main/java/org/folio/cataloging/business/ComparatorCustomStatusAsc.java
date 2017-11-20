package org.folio.cataloging.business;

import java.util.Comparator;

public class ComparatorCustomStatusAsc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 {    	 	       
//-----> Ascending sorting
	     String custom1Status = ((CustomListElement)emp1).getStatusCode();        
	     String custom2Status = ((CustomListElement)emp2).getStatusCode();
	     return custom1Status.toUpperCase().compareTo(custom2Status.toUpperCase());
	 }
}
