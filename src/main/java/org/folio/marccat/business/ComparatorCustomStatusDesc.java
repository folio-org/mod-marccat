package org.folio.cataloging.business;

import java.util.Comparator;

public class ComparatorCustomStatusDesc implements Comparator {
  public int compare(Object emp1, Object emp2) {
//-----> Descending sorting
    String custom1Status = ((CustomListElement) emp1).getStatusCode();
    String custom2Status = ((CustomListElement) emp2).getStatusCode();
    return custom2Status.toUpperCase().compareTo(custom1Status.toUpperCase());
  }
}
