package org.folio.cataloging.business;

import java.util.Comparator;

public class ComparatorCustomCodeAsc implements Comparator {
  public int compare(Object emp1, Object emp2) {
//---->  Ascending sorting
    Integer custom1Code = ((CustomListElement) emp1).getIdCollection ( );
    Integer custom2Code = ((CustomListElement) emp2).getIdCollection ( );
    return custom1Code.compareTo (custom2Code);
  }
}
