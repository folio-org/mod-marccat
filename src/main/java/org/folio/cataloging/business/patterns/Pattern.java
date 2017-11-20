package org.folio.cataloging.business.patterns;

import java.util.Date;

/**
 * <p>
 * </p>
 *  
 */
public interface Pattern {
    /**
     * <p>
     * Implementers of this interface produce an array of dates over a specified
     * range based on a specific date pattern
     * </p>
     * 
     * 
     * @param r
     * @return
     */
    abstract public Date[] calculateDates(Range r);
}

