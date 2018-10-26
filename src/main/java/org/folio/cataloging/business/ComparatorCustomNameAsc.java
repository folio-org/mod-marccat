package org.folio.cataloging.business;

import java.util.Comparator;

public class ComparatorCustomNameAsc implements Comparator {
  public int compare(Object emp1, Object emp2) {
//---->  Ascending sorting  	 	       
    String custom1Name = ((CustomListElement) emp1).getNameIta();
    String custom2Name = ((CustomListElement) emp2).getNameIta();
    return custom1Name.toUpperCase().compareTo(custom2Name.toUpperCase());
  }
}
