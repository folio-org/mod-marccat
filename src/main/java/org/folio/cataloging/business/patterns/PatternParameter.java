/*
 * Created on Dec 17, 2005
 *
 */
package org.folio.cataloging.business.patterns;

import java.util.Calendar;
import java.util.List;

/**
 * @author paul
 *
 */
public class PatternParameter {
    public final static int DAILY_EVERY_N_DAYS = 0;
    public final static int DAILY_EVERY_WEEKDAY = 1;
    public final static int WEEKLY_EVERY_N_WEEKS_ON_LMNOP_DAYS = 2;
    public final static int MONTHLY_ON_DAY_N_EVERY_M_WEEKS = 3;
    public final static int MONTHLY_THE_NTH_M_DAY_OF_EVERY_X_MONTHS = 4;
    public final static int YEARLY_EVERY_M_D = 5;
    public final static int YEARLY_THE_NTH_M_DAY_OF_MONTH_X = 6;

    private int patternClass = YEARLY_THE_NTH_M_DAY_OF_MONTH_X;
    private int ordinal = AbstractPattern.LAST;
    private int timeField = AbstractPattern.FRIDAY;
    private int stepCount = 1;
    private int month = Calendar.JANUARY;
    private int day = 15;
    private int[] daysOfWeek = {AbstractPattern.FRIDAY};
    

    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public int[] getDaysOfWeek() {
        return daysOfWeek;
    }
    public void setDaysOfWeek(int[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
    
    public void setDaysOfWeek(List daysOfWeek) {
        this.daysOfWeek = new int[daysOfWeek.size()];
		for (int i=0; i < daysOfWeek.size(); i++) {
		   this.daysOfWeek[i] = ((Integer)daysOfWeek.get(i)).intValue();
		}
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getOrdinal() {
        return ordinal;
    }
    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
    public int getPatternClass() {
        return patternClass;
    }
    public void setPatternClass(int patternClass) {
        this.patternClass = patternClass;
    }
    public int getStepCount() {
        return stepCount;
    }
    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
    public int getTimeField() {
        return timeField;
    }
    public void setTimeField(int timeField) {
        this.timeField = timeField;
    }
}
