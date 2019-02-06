package org.folio.marccat.business.cataloguing.bibliographic;

import org.folio.marccat.dao.persistence.PublisherAccessPoint;

import java.util.Comparator;

public class PublisherAccessPointComparator implements Comparator {

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(Object arg0, Object arg1) {
    if (arg0 instanceof PublisherAccessPoint
      && arg1 instanceof PublisherAccessPoint) {
      int i = ((PublisherAccessPoint) arg0).getSequenceNumber();
      int j = ((PublisherAccessPoint) arg1).getSequenceNumber();
      if (i < j) {
        return -1;
      } else if (i > j) {
        return +1;
      } else {
        return 0;
      }
    }
    return 0;
  }

}
