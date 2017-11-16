/*
 * (c) LibriCore
 * 
 * Created on 28-ene-2005
 * 
 * CPY_HLD.java
 */
package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class CPY_HLD implements Serializable{
    
    private int copyHoldNumber;
    private int borrowerNumber;
    private int copyIdNumber;
    private short priorityNumber;
    private Date copyHoldCreationDate;
    private char copyTimeHoldIndicator;
    private char copyHoldPrintIndicator;
    private int pickupLocationOrganisationNumber;

    
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
     * @return Returns the copyHoldCreationDate.
     * @exception
     * @since 1.0
     */
    public Date getCopyHoldCreationDate() {
        return copyHoldCreationDate;
    }
    /**
     * @param copyHoldCreationDate The copyHoldCreationDate to set.
     * @exception
     * @since 1.0
     */
    public void setCopyHoldCreationDate(Date copyHoldCreationDate) {
        this.copyHoldCreationDate = copyHoldCreationDate;
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
     * @return Returns the copyHoldPrintIndicator.
     * @exception
     * @since 1.0
     */
    public char getCopyHoldPrintIndicator() {
        return copyHoldPrintIndicator;
    }
    /**
     * @param copyHoldPrintIndicator The copyHoldPrintIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setCopyHoldPrintIndicator(char copyHoldPrintIndicator) {
        this.copyHoldPrintIndicator = copyHoldPrintIndicator;
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
     * @return Returns the copyTimeHoldIndicator.
     * @exception
     * @since 1.0
     */
    public char getCopyTimeHoldIndicator() {
        return copyTimeHoldIndicator;
    }
    /**
     * @param copyTimeHoldIndicator The copyTimeHoldIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setCopyTimeHoldIndicator(char copyTimeHoldIndicator) {
        this.copyTimeHoldIndicator = copyTimeHoldIndicator;
    }
    /**
     * @return Returns the priorityNumber.
     * @exception
     * @since 1.0
     */
    public short getPriorityNumber() {
        return priorityNumber;
    }
    /**
     * @param priorityNumber The priorityNumber to set.
     * @exception
     * @since 1.0
     */
    public void setPriorityNumber(short priorityNumber) {
        this.priorityNumber = priorityNumber;
    }
    /**
     * @return Returns the pickupLocationOrganisationNumber.
     * @exception
     * @since 1.0
     */
    public int getPickupLocationOrganisationNumber() {
        return pickupLocationOrganisationNumber;
    }
    /**
     * @param pickupLocationOrganisationNumber The pickupLocationOrganisationNumber to set.
     * @exception
     * @since 1.0
     */
    public void setPickupLocationOrganisationNumber(
            int pickupLocationOrganisationNumber) {
        this.pickupLocationOrganisationNumber = pickupLocationOrganisationNumber;
    }
}
