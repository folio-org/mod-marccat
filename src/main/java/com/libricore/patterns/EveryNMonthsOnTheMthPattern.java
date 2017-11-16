/*
 * Created on Dec 4, 2005
 *
 */
package com.libricore.patterns;

import java.util.Calendar;
import java.util.Date;

/**
 * @author paul
 *
 */
public class EveryNMonthsOnTheMthPattern extends AbstractPattern {
    private int stepCount;
    private int day;
    private Date firstDate;
    private int callCount = 0;

    EveryNMonthsOnTheMthPattern(int step, int day) {
        setStepCount(step);
        setDay(day);
    }
    
    protected Calendar setFirstDate(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, getDay());
        firstDate = c.getTime();
        return c;
    }
    
    /* (non-Javadoc)
     * @see com.mouland.patterns.AbstractPattern#setNextDate(java.util.Calendar)
     */
    protected Calendar setNextDate(Calendar c) {
        	callCount++;
        	c.setTime(firstDate);
        	c.add(Calendar.MONTH, getStepCount() * callCount);
        	return c;
    }

    public String toString() {
        return  "Every " + getStepCount() + " month(s) on day " + getDay();
    }
    protected int getDay() {
        return day;
    }
    protected void setDay(int day) {
        this.day = day;
    }
    protected int getStepCount() {
        return stepCount;
    }
    protected void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
