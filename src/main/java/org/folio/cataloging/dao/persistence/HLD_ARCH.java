/*
 * (c) LibriCore
 * 
 * Created on 20-jun-2005
 * 
 * HLD_ARCH.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

 /**
 * @author Ruben
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class HLD_ARCH implements Serializable{
    
    private int holdArchNumber;
    private int copyIdNumber;
    private int borrowerNumber;
    private int bibItemNumber;
    private Date creationDate;
    private char timeHoldIndicator;
    private Date archDate;
    private Date holdOnShelfDate;
    private Date holdTimeStartingDate;
    private Date holdTimeEndDate;
    
    
    
    

    /**
     * @return Returns the holdTimeEndDate.
     * @exception
     * @since 1.0
     */
    public Date getHoldTimeEndDate() {
        return holdTimeEndDate;
    }
    /**
     * @param holdTimeEndDate The holdTimeEndDate to set.
     * @exception
     * @since 1.0
     */
    public void setHoldTimeEndDate(Date holdTimeEndDate) {
        this.holdTimeEndDate = holdTimeEndDate;
    }
    /**
     * @return Returns the holdTimeStartingDate.
     * @exception
     * @since 1.0
     */
    public Date getHoldTimeStartingDate() {
        return holdTimeStartingDate;
    }
    /**
     * @param holdTimeStartingDate The holdTimeStartingDate to set.
     * @exception
     * @since 1.0
     */
    public void setHoldTimeStartingDate(Date holdTimeStartingDate) {
        this.holdTimeStartingDate = holdTimeStartingDate;
    }
    /**
     * @return Returns the archDate.
     * @exception
     * @since 1.0
     */
    public Date getArchDate() {
        return archDate;
    }
    /**
     * @param archDate The archDate to set.
     * @exception
     * @since 1.0
     */
    public void setArchDate(Date archDate) {
        this.archDate = archDate;
    }
    /**
     * @return Returns the bibItemNumber.
     * @exception
     * @since 1.0
     */
    public int getBibItemNumber() {
        return bibItemNumber;
    }
    /**
     * @param bibItemNumber The bibItemNumber to set.
     * @exception
     * @since 1.0
     */
    public void setBibItemNumber(int bibItemNumber) {
        this.bibItemNumber = bibItemNumber;
    }
    /**
     * @return Returns the borrowerNumber.
     * @exception
     * @since 1.0
     */
    public int getBorrowerNumber() {
        return borrowerNumber;
    }
    /**
     * @param borrowerNumber The borrowerNumber to set.
     * @exception
     * @since 1.0
     */
    public void setBorrowerNumber(int borrowerNumber) {
        this.borrowerNumber = borrowerNumber;
    }
    
    /**
     * @return Returns the copyIdNumber.
     * @exception
     * @since 1.0
     */
    public int getCopyIdNumber() {
        return copyIdNumber;
    }
    /**
     * @param copyIdNumber The copyIdNumber to set.
     * @exception
     * @since 1.0
     */
    public void setCopyIdNumber(int copyIdNumber) {
        this.copyIdNumber = copyIdNumber;
    }
    /**
     * @return Returns the creationDate.
     * @exception
     * @since 1.0
     */
    public Date getCreationDate() {
        return creationDate;
    }
    /**
     * @param creationDate The creationDate to set.
     * @exception
     * @since 1.0
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * @return Returns the holdArchNumber.
     * @exception
     * @since 1.0
     */
    public int getHoldArchNumber() {
        return holdArchNumber;
    }
    /**
     * @param holdArchNumber The holdArchNumber to set.
     * @exception
     * @since 1.0
     */
    public void setHoldArchNumber(int holdArchNumber) {
        this.holdArchNumber = holdArchNumber;
    }
    /**
     * @return Returns the holdOnShelfDate.
     * @exception
     * @since 1.0
     */
    public Date getHoldOnShelfDate() {
        return holdOnShelfDate;
    }
    /**
     * @param holdOnShelfDate The holdOnShelfDate to set.
     * @exception
     * @since 1.0
     */
    public void setHoldOnShelfDate(Date holdOnShelfDate) {
        this.holdOnShelfDate = holdOnShelfDate;
    }
    /**
     * @return Returns the timeHoldIndicator.
     * @exception
     * @since 1.0
     */
    public char getTimeHoldIndicator() {
        return timeHoldIndicator;
    }
    /**
     * @param timeHoldIndicator The timeHoldIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setTimeHoldIndicator(char timeHoldIndicator) {
        this.timeHoldIndicator = timeHoldIndicator;
    }
}
