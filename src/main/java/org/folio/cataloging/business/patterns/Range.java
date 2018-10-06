package org.folio.cataloging.business.patterns;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Represents the range (start/stop) of a pattern
 * </p>
 */
public class Range {
  private Date startDate;

  private Integer repeatCount;

  private Date endDate;

  Range(Date start, int repeat) {
    setStartDate (start);
    setRepeatCount (new Integer (repeat));
  }

  Range(Date start, Date end) {
    setStartDate (start);
    setEndDate (end);
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Integer getRepeatCount() {
    return repeatCount;
  }

  public void setRepeatCount(Integer repeatCount) {
    this.repeatCount = repeatCount;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public String toString() {
    DateFormat df = new SimpleDateFormat ("EEE dd MM yyyy");
    String result = " from " + df.format (getStartDate ( ));
    if (getRepeatCount ( ) == null) {
      result = result + " until " + df.format (getEndDate ( ));
    } else {
      result = result + " for " + getRepeatCount ( ) + " occurrences ";
    }

    return result;
  }
}
