/*
 * Created on Dec 17, 2005
 *
 */
package org.folio.cataloging.business.patterns;

/**
 * Constructs calendar Pattern objects based on the specified parameters
 *
 * @author paul
 */
public class PatternFactory {

  public static Pattern createPattern(PatternParameter p) {
    switch (p.getPatternClass()) {
      case PatternParameter.DAILY_EVERY_N_DAYS:
        return new EveryNDaysPattern(p.getStepCount());
      case PatternParameter.DAILY_EVERY_WEEKDAY:
        return new EveryWeekdayPattern();
      case PatternParameter.WEEKLY_EVERY_N_WEEKS_ON_LMNOP_DAYS:
        return new EveryNthMdayPattern(p.getStepCount(), p.getDaysOfWeek());
      case PatternParameter.MONTHLY_ON_DAY_N_EVERY_M_WEEKS:
        return new EveryNMonthsOnTheMthPattern(p.getStepCount(), p.getDay());
      case PatternParameter.MONTHLY_THE_NTH_M_DAY_OF_EVERY_X_MONTHS:
        return new EveryNMonthsOnTheXthYPattern(p.getOrdinal(), p.getTimeField(), p.getStepCount());
      case PatternParameter.YEARLY_EVERY_M_D:
        return new Anniversary(p.getMonth(), p.getDay());
      case PatternParameter.YEARLY_THE_NTH_M_DAY_OF_MONTH_X:
        return new SecondSaturdayOfDecember(p.getOrdinal(), p.getTimeField(), p.getMonth());
      default:
        throw new IllegalArgumentException("Unknown pattern class");
    }
  }
}
