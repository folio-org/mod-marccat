package org.folio.cataloging.business;

import java.util.Comparator;

public class ComparatorCustomCodeDesc implements Comparator {
  public int compare(Object emp1, Object emp2) {
    Integer custom1Code = ((CustomListElement) emp1).getIdCollection ( );
    Integer custom2Code = ((CustomListElement) emp2).getIdCollection ( );
    return custom2Code.compareTo (custom1Code);
  }
}
