package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.dao.persistence.PUBL_TAG;

import java.util.Comparator;

public class PublisherTagComparator implements Comparator {

  public int compare(final Object arg0, final Object arg1) {
    PUBL_TAG tm1 = (PUBL_TAG) arg0;
    PUBL_TAG tm2 = (PUBL_TAG) arg1;
    int i = tm1.getSequenceNumber();
    int j = tm2.getSequenceNumber();
    if (i < j) {
      return -1;
    } else if (i > j) {
      return +1;
    } else {
      return 0;
    }
  }


}
