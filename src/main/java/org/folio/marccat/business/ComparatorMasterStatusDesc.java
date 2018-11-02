package org.folio.marccat.business;

import java.util.Comparator;

public class ComparatorMasterStatusDesc implements Comparator {
  public int compare(Object emp1, Object emp2) {
//-----> Descending sorting
    String master1Status = ((MasterListElement) emp1).getStatusCode();
    String master2Status = ((MasterListElement) emp2).getStatusCode();
    return master2Status.toUpperCase().compareTo(master1Status.toUpperCase());
  }
}
