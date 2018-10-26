/*
 * (c) LibriCore
 *
 * Created on Aug 28, 2004
 *
 * TagComparator.java
 */
package org.folio.cataloging.business.common.group;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.cataloguing.bibliographic.MarcCorrelationException;
import org.folio.cataloging.business.cataloguing.common.Tag;
import org.folio.cataloging.business.common.DataAccessException;

import java.util.Comparator;

/**
 * @author paulm
 * @version $Revision: 1.4 $, $Date: 2005/12/01 13:50:04 $
 * @since 1.0
 */
public class GroupComparator implements Comparator {
  private static Log logger = LogFactory.getLog(GroupComparator.class);

  /*
   * (non-Javadoc)
   *
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(Object arg0, Object arg1) {
    if (arg0 == arg1) return 0;

    // recupera il tag leader del gruppo 0
    Tag t0 = null;
    if (arg0 instanceof Tag) t0 = (Tag) arg0;
    else t0 = ((TagContainer) arg0).getLeaderTag();

    // recupera il tag leader del gruppo 1
    Tag t1 = null;
    if (arg1 instanceof Tag) t1 = (Tag) arg1;
    else t1 = ((TagContainer) arg1).getLeaderTag();

    return compareTagNumber(t0, t1);
  }

  protected int compareTagNumber(Object t0, Object t1) {
    try {
      return ((Tag) t0).getMarcEncoding().getMarcTag().compareTo(
        ((Tag) t1).getMarcEncoding().getMarcTag());
    } catch (MarcCorrelationException e) {
      logger.warn("MarcCorrelationException sorting in tag order");
      return 0;
    } catch (DataAccessException e) {
      logger.warn("DataAccessException sorting in tag order");
      return 0;
    }
  }
}
