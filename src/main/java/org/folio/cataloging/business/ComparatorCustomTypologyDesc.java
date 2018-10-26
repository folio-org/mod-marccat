package org.folio.cataloging.business;

import java.util.Comparator;

public class ComparatorCustomTypologyDesc implements Comparator {
  public int compare(Object emp1, Object emp2) {
//-----> Descending sorting
    String custom1Tipology = ((CustomListElement) emp1).getTypologyCode();
    String custom2Tipology = ((CustomListElement) emp2).getTypologyCode();
    return custom2Tipology.toUpperCase().compareTo(custom1Tipology.toUpperCase());
  }
}
