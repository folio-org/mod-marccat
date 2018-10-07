/*
 * Created on Dec 10, 2005
 *
 */
package org.folio.cataloging.business.patterns;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * @author paul
 */
public class SecondSaturdayOfDecember extends AbstractPattern {
  private int occurrence;

  private int timeField;

  private int month;

  SecondSaturdayOfDecember(int occ, int time, int month) {
    setOccurrence (occ);
    setTimeField (time);
    setMonth (month);
  }

  protected Calendar setFirstDate(Calendar c) {
    Calendar result = new GregorianCalendar ( );
    result.setTime (c.getTime ( ));
    result = applyPattern (result);
    if (result.before (c)) {
      result.setTime (c.getTime ( ));
      result.add (Calendar.YEAR, 1);
      result = applyPattern (result);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.mouland.patterns.AbstractPattern#setNextDate(java.util.Calendar)
   */
  protected Calendar setNextDate(Calendar c) {
    c.add (Calendar.YEAR, 1);
    return applyPattern (c);
  }

  public String toString() {
    return "The " + occurText (getOccurrence ( )) + " "
      + timeText (getTimeField ( )) + " of " + monthText (getMonth ( ));
  }

  private Calendar applyPattern(Calendar c) {
    Calendar result;
    if (getOccurrence ( ) == AbstractPattern.LAST) {
      if (getTimeField ( ) == AbstractPattern.WEEKDAY) {
        result = lastWeekdayOfMonthX (c, getMonth ( ));
      } else if (getTimeField ( ) == AbstractPattern.WEEKEND) {
        result = lastWeekendDayOfMonthX (c, getMonth ( ));
      } else {
        result = secondSaturdayOfDecember (c, getOccurrence ( ), getTimeField ( ), getMonth ( ));
      }
    } else {
      if (getTimeField ( ) == AbstractPattern.WEEKDAY) {
        result = secondWeekdayOfMonthX (c, getOccurrence ( ), getMonth ( ));
      } else if (getTimeField ( ) == AbstractPattern.WEEKEND) {
        result = secondWeekendDayOfMonthX (c, getOccurrence ( ), getMonth ( ));
      } else {
        result = secondSaturdayOfDecember (c, getOccurrence ( ), getTimeField ( ), getMonth ( ));
      }
    }
    return result;
  }

  private Calendar secondSaturdayOfDecember(Calendar c, int occ, int timeField, int month) {
    c.set (Calendar.MONTH, month);
    c.set (Calendar.DAY_OF_WEEK, timeField);
    c.set (Calendar.DAY_OF_WEEK_IN_MONTH, occ);
    return c;
  }

  private Calendar lastWeekdayOfMonthX(Calendar c, int month) {
    c.set (Calendar.MONTH, month + 1);
    c.set (Calendar.DATE, -0);
    while (c.get (Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get (Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      c.add (Calendar.DATE, -1);
    }
    return c;
  }

  private Calendar lastWeekendDayOfMonthX(Calendar c, int month) {
    c.set (Calendar.MONTH, month + 1);
    c.set (Calendar.DATE, -0);
    while (c.get (Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get (Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
      c.add (Calendar.DATE, -1);
    }
    return c;
  }

  private Calendar secondWeekdayOfMonthX(Calendar c, int occ, int month) {
    c.set (Calendar.MONTH, month);
    c.set (Calendar.DATE, 1);
    while (occ > 0) {
      if (c.get (Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get (Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        // do nothing
      } else {
        occ--;
      }
      if (occ > 0) {
        c.add (Calendar.DATE, 1);
      }
    }
    return c;
  }

  private Calendar secondWeekendDayOfMonthX(Calendar c, int occ, int month) {
    c.set (Calendar.MONTH, month);
    c.set (Calendar.DATE, 1);
    while (occ > 0) {
      if (c.get (Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get (Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        occ--;
      } else {
        // do nothing
      }
      if (occ > 0) {
        c.add (Calendar.DATE, 1);
      }
    }
    return c;
  }

  private int getMonth() {
    return month;
  }

  private void setMonth(int month) {
    this.month = month;
  }

  private int getOccurrence() {
    return occurrence;
  }

  private void setOccurrence(int occurrence) {
    this.occurrence = occurrence;
  }

  private int getTimeField() {
    return timeField;
  }

  private void setTimeField(int timeField) {
    this.timeField = timeField;
  }
}
