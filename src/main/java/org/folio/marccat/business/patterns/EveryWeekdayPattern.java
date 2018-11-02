/*
 * Created on Dec 3, 2005
 *
 */
package org.folio.marccat.business.patterns;

import java.util.Calendar;

/**
 * @author paul
 */
public class EveryWeekdayPattern extends AbstractPattern {

  protected Calendar setFirstDate(Calendar c) {
    if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
      c.add(Calendar.DATE, 2);
    } else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      c.add(Calendar.DATE, 1);
    }
    return c;
  }

  /* (non-Javadoc)
   * @see com.mouland.patterns.AbstractPattern#setNextDate(java.util.Calendar)
   */
  protected Calendar setNextDate(Calendar c) {
    if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
      c.add(Calendar.DATE, 3);
    } else {
      c.add(Calendar.DATE, 1);
    }
    return c;
  }

  public String toString() {
    return "Every weekday ";
  }
}
