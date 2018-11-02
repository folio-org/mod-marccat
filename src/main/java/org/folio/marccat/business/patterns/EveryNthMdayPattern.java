/*
 * Created on Dec 3, 2005
 *
 */
package org.folio.marccat.business.patterns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * @author paul
 */
public class EveryNthMdayPattern extends AbstractPattern {

  private int stepCount;
  private TreeSet days;

  EveryNthMdayPattern(int step, int[] daysOfWeek) {
    setStepCount(step);
    this.days = new TreeSet();
    for (int i = 0; i < daysOfWeek.length; i++) {
      days.add(new Integer(daysOfWeek[i]));
    }
  }

  protected Calendar setFirstDate(Calendar c) {
    Iterator iter = days.iterator();
    while (iter.hasNext()) {
      int next = ((Integer) iter.next()).intValue();
      if (c.get(Calendar.DAY_OF_WEEK) <= next) {
        c.set(Calendar.DAY_OF_WEEK, next);
        return c;
      }
    }
    /*
     * if we made it here then the start date is later than latest day_of_week
     */
    c.set(Calendar.DAY_OF_WEEK, ((Integer) days.first()).intValue());
    c.add(Calendar.WEEK_OF_YEAR, 1);
    return c;

  }

  /* (non-Javadoc)
   * @see com.mouland.patterns.AbstractPattern#setNextDate(java.util.Calendar)
   */
  protected Calendar setNextDate(Calendar c) {
    Iterator iter = days.iterator();
    while (iter.hasNext()) {
      int next = ((Integer) iter.next()).intValue();
      if (c.get(Calendar.DAY_OF_WEEK) < next) {
        c.set(Calendar.DAY_OF_WEEK, next);
        return c;
      }
    }
    /*
     * if we made it here then the start date is later than latest day_of_week
     */
    c.set(Calendar.DAY_OF_WEEK, ((Integer) days.first()).intValue());
    c.add(Calendar.WEEK_OF_YEAR, getStepCount());
    return c;
  }

  public int getStepCount() {
    return stepCount;
  }

  public void setStepCount(int stepCount) {
    this.stepCount = stepCount;
  }

  public String toString() {
    DateFormat df = new SimpleDateFormat("EEEEE");
    Calendar c = new GregorianCalendar();
    String result = "Every " + getStepCount() + " week(s) on ";
    Iterator iter = days.iterator();
    while (iter.hasNext()) {
      c.set(Calendar.DAY_OF_WEEK, ((Integer) iter.next()).intValue());
      result = result + df.format(c.getTime()) + " ";
    }
    return result;
  }
}
