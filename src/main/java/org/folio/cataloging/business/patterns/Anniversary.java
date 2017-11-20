/*
 * Created on Dec 10, 2005
 *
 */
package org.folio.cataloging.business.patterns;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author paul
 *
 */
public class Anniversary extends AbstractPattern {
    private int month;
    private int day;
    
    public Anniversary(int month, int day) {
        setMonth(month);
        setDay(day);
    }
    protected Calendar setFirstDate(Calendar c) {
        Calendar result = new GregorianCalendar();
        result.setTime(c.getTime());
        result.set(Calendar.MONTH, getMonth());
        result.set(Calendar.DATE, getDay());
        if (result.before(c)) {
            result.add(Calendar.YEAR, 1);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.mouland.patterns.AbstractPattern#setNextDate(java.util.Calendar)
     */
    protected Calendar setNextDate(Calendar c) {
        c.add(Calendar.YEAR, 1);
        return c;
    }

    private int getDay() {
        return day;
    }
    private void setDay(int day) {
        this.day = day;
    }
    private int getMonth() {
        return month;
    }
    private void setMonth(int month) {
        this.month = month;
    }
    public String toString() {
        return "Every " + monthText(getMonth()) + " " + getDay();
    }
}
