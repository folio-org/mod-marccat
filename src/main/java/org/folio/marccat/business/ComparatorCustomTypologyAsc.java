package org.folio.marccat.business;

import java.util.Comparator;

public class ComparatorCustomTypologyAsc implements Comparator {
  public int compare(Object emp1, Object emp2) {
//---->	 Ascending sorting
    String custom1Tipology = ((CustomListElement) emp1).getTypologyCode();
    String custom2Tipology = ((CustomListElement) emp2).getTypologyCode();
    return custom1Tipology.toUpperCase().compareTo(custom2Tipology.toUpperCase());
  }
}
