/*
 * Created on Dec 5, 2005
 *
 */
package org.folio.marccat.business.patterns;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * The 1st Monday of every second month
 *
 * @author paul
 */
public class EveryNMonthsOnTheXthYPattern extends AbstractPattern {

  private int occurrence;

  private int timeField;

  private int stepCount;

  EveryNMonthsOnTheXthYPattern(int occ, int time, int step) {
    setOccurrence(occ);
    setTimeField(time);
    setStepCount(step);
  }

  protected Calendar setFirstDate(Calendar c) {
    Calendar result = new GregorianCalendar();
    result.setTime(c.getTime());
    result = applyPattern(result);
    if (result.before(c)) {
      result.setTime(c.getTime());
      result.add(Calendar.MONTH, 1);
      result = applyPattern(result);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.mouland.patterns.AbstractPattern#setNextDate(java.util.Calendar)
   */
  protected Calendar setNextDate(Calendar c) {
    c.add(Calendar.MONTH, getStepCount());
    return applyPattern(c);
  }

  public String toString() {
    return "The " + occurText(getOccurrence()) + " "
      + timeText(getTimeField()) + " of every " + getStepCount()
      + " month(s)";
  }

  private Calendar applyPattern(Calendar c) {
    Calendar result;
    if (getOccurrence() == AbstractPattern.LAST) {
      if (getTimeField() == AbstractPattern.WEEKDAY) {
        result = lastWeekdayOfTheMonth(c);
      } else if (getTimeField() == AbstractPattern.WEEKEND) {
        result = lastWeekendDayOfTheMonth(c);
      } else {
        result = secondMondayOfTheMonth(c, getOccurrence(), getTimeField());
      }
    } else {
      if (getTimeField() == AbstractPattern.WEEKDAY) {
        result = secondWeekdayOfTheMonth(c, getOccurrence());
      } else if (getTimeField() == AbstractPattern.WEEKEND) {
        result = secondWeekendDayOfTheMonth(c, getOccurrence());
      } else {
        result = secondMondayOfTheMonth(c, getOccurrence(), getTimeField());
      }
    }
    return result;
  }

  private int getOccurrence() {
    return occurrence;
  }

  private void setOccurrence(int occurrence) {
    this.occurrence = occurrence;
  }

  private int getStepCount() {
    return stepCount;
  }

  private void setStepCount(int stepCount) {
    this.stepCount = stepCount;
  }

  private int getTimeField() {
    return timeField;
  }

  private void setTimeField(int timeField) {
    this.timeField = timeField;
  }
}
