/*
 * Created on Dec 3, 2005
 *
 */
package org.folio.cataloging.business.patterns;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author paul
 */
public abstract class AbstractPattern implements Pattern {


  public final static int MONDAY = Calendar.MONDAY;
  public final static int TUESDAY = Calendar.TUESDAY;
  public final static int WEDNESDAY = Calendar.WEDNESDAY;
  public final static int THURSDAY = Calendar.THURSDAY;
  public final static int FRIDAY = Calendar.FRIDAY;
  public final static int SATURDAY = Calendar.SATURDAY;
  public final static int SUNDAY = Calendar.SUNDAY;
  public final static int WEEKDAY = -1;
  public final static int WEEKEND = -2;

  public final static int LAST = -1;
  static Map monthText = new HashMap();
  static Map occurText = new HashMap();
  static Map timeText = new HashMap();

  {
    monthText.put(new Integer(Calendar.JANUARY), "January");
    monthText.put(new Integer(Calendar.FEBRUARY), "February");
    monthText.put(new Integer(Calendar.MARCH), "March");
    monthText.put(new Integer(Calendar.APRIL), "April");
    monthText.put(new Integer(Calendar.MAY), "May");
    monthText.put(new Integer(Calendar.JUNE), "June");
    monthText.put(new Integer(Calendar.JULY), "July");
    monthText.put(new Integer(Calendar.AUGUST), "August");
    monthText.put(new Integer(Calendar.SEPTEMBER), "September");
    monthText.put(new Integer(Calendar.OCTOBER), "October");
    monthText.put(new Integer(Calendar.NOVEMBER), "November");
    monthText.put(new Integer(Calendar.DECEMBER), "December");
  }

  {
    occurText.put(new Integer(1), "first");
    occurText.put(new Integer(2), "second");
    occurText.put(new Integer(3), "third");
    occurText.put(new Integer(4), "fourth");
    occurText.put(new Integer(AbstractPattern.LAST), "last");
  }

  {
    timeText.put(new Integer(AbstractPattern.MONDAY), "Monday");
    timeText.put(new Integer(AbstractPattern.TUESDAY), "Tuesday");
    timeText.put(new Integer(AbstractPattern.WEDNESDAY), "Wednesday");
    timeText.put(new Integer(AbstractPattern.THURSDAY), "Thursday");
    timeText.put(new Integer(AbstractPattern.FRIDAY), "Friday");
    timeText.put(new Integer(AbstractPattern.SATURDAY), "Saturday");
    timeText.put(new Integer(AbstractPattern.SUNDAY), "Sunday");
    timeText.put(new Integer(AbstractPattern.WEEKDAY), "weekday");
    timeText.put(new Integer(AbstractPattern.WEEKEND), "weekend day");
  }

  protected static String monthText(int i) {
    return (String) monthText.get(new Integer(i));
  }

  protected static String occurText(int i) {
    return (String) occurText.get(new Integer(i));
  }

  protected static String timeText(int i) {
    return (String) timeText.get(new Integer(i));
  }

  protected static String printDate(Calendar c) {
    /* we clone here because calls to getTime will modify the internal settings of the Calendar object */
    Calendar d = (Calendar) c.clone();
    return new SimpleDateFormat("EEE dd MM yyyy").format(d.getTime());

  }

  public Date[] calculateDates(Range r) {
    List result = new ArrayList();
    Calendar c = new GregorianCalendar();
    c.setTime(r.getStartDate());
    c.set(Calendar.HOUR, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c = setFirstDate(c);
    if (r.getRepeatCount() != null) {
      for (int i = 0; i < r.getRepeatCount().intValue(); i++) {
        result.add(c.getTime());
        c = setNextDate(c);
      }
    } else {
      while (!c.getTime().after(r.getEndDate())) {
        result.add(c.getTime());
        c = setNextDate(c);
      }
    }
    return (Date[]) result.toArray(new Date[0]);
  }

  /**
   * invoked before iterating through the range.  Subclasses may override to adjust initial start date.
   */
  protected Calendar setFirstDate(Calendar c) {
    return c;
  }

  abstract protected Calendar setNextDate(Calendar c);

  protected Calendar secondMondayOfTheMonth(Calendar c, int occ, int day) {
    c.set(Calendar.DAY_OF_WEEK, day);
    c.set(Calendar.DAY_OF_WEEK_IN_MONTH, occ);
    return c;
  }

  protected Calendar secondWeekdayOfTheMonth(Calendar c, int occ) {
    c.set(Calendar.DATE, 1);
    while (occ > 0) {
      if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        // do nothing
      } else {
        occ--;
      }
      if (occ > 0) {
        c.add(Calendar.DATE, 1);
      }
    }
    return c;
  }

  protected Calendar secondWeekendDayOfTheMonth(Calendar c, int occ) {
    c.set(Calendar.DATE, 1);
    while (occ > 0) {
      if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        occ--;
      } else {
        // do nothing
      }
      if (occ > 0) {
        c.add(Calendar.DATE, 1);
      }
    }
    return c;
  }

  protected Calendar lastWeekdayOfTheMonth(Calendar c) {
    c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
    c.set(Calendar.DATE, -0);
    while (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      c.add(Calendar.DATE, -1);
    }
    return c;
  }

  protected Calendar lastWeekendDayOfTheMonth(Calendar c) {
    c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
    c.set(Calendar.DATE, -0);
    while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
      c.add(Calendar.DATE, -1);
    }
    return c;
  }

}

