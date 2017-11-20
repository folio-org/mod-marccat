package org.folio.cataloging.business;

import java.util.Comparator;

public class ComparatorMasterNameDesc implements Comparator 
{
	 public int compare(Object emp1, Object emp2)
	 {    	 	       
//---->  Descending sorting 
	     String master1Name = ((MasterListElement)emp1).getNameIta();        
	     String master2Name = ((MasterListElement)emp2).getNameIta();
	     
	     return master2Name.toUpperCase().compareTo(master1Name.toUpperCase());
	 }
}
