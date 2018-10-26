/*
 * (c) LibriCore
 *
 * Created on 12 Jun 2007
 *
 * PredictionPatternDetail.java
 */
package org.folio.cataloging.business.patterns;

import java.util.List;

public class PredictionPatternDetail {
  public int predictionPatternNumber;
  public int sequenceNumber;
  public PatternParameter pattern = new PatternParameter();

  public int getDay() {
    return pattern.getDay();
  }

  public void setDay(int day) {
    pattern.setDay(day);
  }

  public int[] getDaysOfWeek() {
    return pattern.getDaysOfWeek();
  }

  public void setDaysOfWeek(int[] daysOfWeek) {
    pattern.setDaysOfWeek(daysOfWeek);
  }

  public void setDaysOfWeek(List daysOfWeek) {
    pattern.setDaysOfWeek(daysOfWeek);
  }

  public int getMonth() {
    return pattern.getMonth();
  }

  public void setMonth(int month) {
    pattern.setMonth(month);
  }

  public int getOrdinal() {
    return pattern.getOrdinal();
  }

  public void setOrdinal(int ordinal) {
    pattern.setOrdinal(ordinal);
  }

  public int getPatternClass() {
    return pattern.getPatternClass();
  }

  public void setPatternClass(int patternClass) {
    pattern.setPatternClass(patternClass);
  }

  public int getStepCount() {
    return pattern.getStepCount();
  }

  public void setStepCount(int stepCount) {
    pattern.setStepCount(stepCount);
  }

  public int getTimeField() {
    return pattern.getTimeField();
  }

  public void setTimeField(int timeField) {
    pattern.setTimeField(timeField);
  }

  public PatternParameter getPattern() {
    return pattern;
  }

  public void setPattern(PatternParameter pattern) {
    this.pattern = pattern;
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
}
