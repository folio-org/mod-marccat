/*
 * (c) LibriCore
 * 
 * Created on 28-ene-2005
 * 
 * CPY_HLD_DTL.java
 */
package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Elena
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class CPY_HLD_DTL implements Serializable{

    private int copyHoldDetailNumber;
    private int copyHoldNumber; 
    private Date copyHoldOnShelfDate;
    private Date copyHoldTimeStartingDate;
    private Date copyHoldTimeEndDate;
    private char copyHoldTimeStatusIndicator;
    private char holdRecallQueueTypeCode;

    
    /**
     * @return Returns the copyHoldDetailNumber.
     * @exception
     * @since 1.0
     */
    public int getCopyHoldDetailNumber() {
        return copyHoldDetailNumber;
    }
    /**
     * @param copyHoldDetailNumber The copyHoldDetailNumber to set.
     * @exception
     * @since 1.0
     */
    public void setCopyHoldDetailNumber(int copyHoldDetailNumber) {
        this.copyHoldDetailNumber = copyHoldDetailNumber;
    }
    /**
     * @return Returns the copyHoldNumber.
     * @exception
     * @since 1.0
     */
    public int getCopyHoldNumber() {
        return copyHoldNumber;
    }
    /**
     * @param copyHoldNumber The copyHoldNumber to set.
     * @exception
     * @since 1.0
     */
    public void setCopyHoldNumber(int copyHoldNumber) {
        this.copyHoldNumber = copyHoldNumber;
    }
    /**
     * @return Returns the copyHoldOnShelfDate.
     * @exception
     * @since 1.0
     */
    public Date getCopyHoldOnShelfDate() {
        return copyHoldOnShelfDate;
    }
    /**
     * @param copyHoldOnShelfDate The copyHoldOnShelfDate to set.
     * @exception
     * @since 1.0
     */
    public void setCopyHoldOnShelfDate(Date copyHoldOnShelfDate) {
        this.copyHoldOnShelfDate = copyHoldOnShelfDate;
    }
    /**
     * @return Returns the copyHoldTimeEndDate.
     * @exception
     * @since 1.0
     */
    public Date getCopyHoldTimeEndDate() {
        return copyHoldTimeEndDate;
    }
    /**
     * @param copyHoldTimeEndDate The copyHoldTimeEndDate to set.
     * @exception
     * @since 1.0
     */
    public void setCopyHoldTimeEndDate(Date copyHoldTimeEndDate) {
        this.copyHoldTimeEndDate = copyHoldTimeEndDate;
    }
    /**
     * @return Returns the copyHoldTimeStartingDate.
     * @exception
     * @since 1.0
     */
    public Date getCopyHoldTimeStartingDate() {
        return copyHoldTimeStartingDate;
    }
    /**
     * @param copyHoldTimeStartingDate The copyHoldTimeStartingDate to set.
     * @exception
     * @since 1.0
     */
    public void setCopyHoldTimeStartingDate(Date copyHoldTimeStartingDate) {
        this.copyHoldTimeStartingDate = copyHoldTimeStartingDate;
    }
    /**
     * @return Returns the copyHoldTimeStatusIndicator.
     * @exception
     * @since 1.0
     */
    public char getCopyHoldTimeStatusIndicator() {
        return copyHoldTimeStatusIndicator;
    }
    /**
     * @param copyHoldTimeStatusIndicator The copyHoldTimeStatusIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setCopyHoldTimeStatusIndicator(char copyHoldTimeStatusIndicator) {
        this.copyHoldTimeStatusIndicator = copyHoldTimeStatusIndicator;
    }
 
    /**
     * @return Returns the holdRecallQueueTypeCode.
     * @exception
     * @since 1.0
     */
    public char getHoldRecallQueueTypeCode() {
        return holdRecallQueueTypeCode;
    }
    /**
     * @param holdRecallQueueTypeCode The holdRecallQueueTypeCode to set.
     * @exception
     * @since 1.0
     */
    public void setHoldRecallQueueTypeCode(
            char holdRecallQueueTypeCode) {
        this.holdRecallQueueTypeCode = holdRecallQueueTypeCode;
    }
}
