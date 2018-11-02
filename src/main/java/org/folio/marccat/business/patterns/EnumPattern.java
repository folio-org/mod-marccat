/*
 * (c) LibriCore
 *
 * Created on 12 Jun 2007
 *
 * EnumPattern.java
 */
package org.folio.marccat.business.patterns;

import java.util.Date;

public class EnumPattern {
  private int predictionPatternNumber;

  private int sequenceNumber;

  private boolean numberingContinuous;

  private int numberOfUnits;

  private int startAt;

  private Date startAtDate;

  private int lastValueUsed;

  private int lowerLevelCount;

  public int getLowerLevelCount() {
    return lowerLevelCount;
  }

  public void setLowerLevelCount(int lowerLevelCount) {
    this.lowerLevelCount = lowerLevelCount;
  }

  public int getLastValueUsed() {
    return lastValueUsed;
  }

  public void setLastValueUsed(int lastValueUsed) {
    this.lastValueUsed = lastValueUsed;
  }

  public boolean isNumberingContinuous() {
    return numberingContinuous;
  }

  public void setNumberingContinuous(boolean numberingContinuous) {
    this.numberingContinuous = numberingContinuous;
  }

  public int getNumberOfUnits() {
    return numberOfUnits;
  }

  public void setNumberOfUnits(int numberOfUnits) {
    this.numberOfUnits = numberOfUnits;
  }

  public int getPredictionPatternNumber() {
    return predictionPatternNumber;
  }

  public void setPredictionPatternNumber(int predictionPatternNumber) {
    this.predictionPatternNumber = predictionPatternNumber;
  }

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public int getStartAt() {
    return startAt;
  }

  public void setStartAt(int startAt) {
    this.startAt = startAt;
  }

  public Date getStartAtDate() {
    return startAtDate;
  }

  public void setStartAtDate(Date startAtDate) {
    this.startAtDate = startAtDate;
  }
}
