package org.folio.marccat.business;

import java.util.Comparator;

public class ComparatorCustomNameDesc implements Comparator {
  public int compare(Object emp1, Object emp2) {
//---->  Descending sorting 
    String custom1Name = ((CustomListElement) emp1).getNameIta();
    String custom2Name = ((CustomListElement) emp2).getNameIta();

    return custom2Name.toUpperCase().compareTo(custom1Name.toUpperCase());
  }
}
