package org.folio.cataloging.business;

import java.util.Comparator;

public class ComparatorMasterTypologyAsc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 {    	 	       
//---->	 Ascending sorting
	     String master1Tipology = ((MasterListElement)emp1).getTypologyCode();        
	     String master2Tipology = ((MasterListElement)emp2).getTypologyCode();
	     return master1Tipology.toUpperCase().compareTo(master2Tipology.toUpperCase());
	 }
}
