package org.folio.marccat.business.patterns;

import java.util.Calendar;


/**
 * <p></p>
 */
public class EveryNDaysPattern extends AbstractPattern {
  private int stepCount;

  /**
   * Constructer
   *
   * @param step the step count
   */
  EveryNDaysPattern(int step) {
    setStepCount(step);
  }

  public int getStepCount() {
    return stepCount;
  }

  public void setStepCount(int stepCount) {
    this.stepCount = stepCount;
  }

  protected Calendar setNextDate(Calendar c) {
    c.add(Calendar.DATE, getStepCount());
    return c;
  }

  public String toString() {
    return "Every " + getStepCount() + " days ";
  }
}
