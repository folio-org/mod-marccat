/*
 * (c) LibriCore
 * 
 * Created on 28-ene-2005
 * 
 * TTL_HLD.java
 */
package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Elena
 * @version $Revision: 1.1 $, $Date: 2005/07/25 13:09:24 $
 * @since 1.0
 */
public class TTL_HLD implements Serializable{

    private int titleHoldNumber;
    private int borrowerNumber;
    private int bibItemNumber;
    private short priorityNumber;
    private Date titleHoldCreationDate;
    private char titleTimeHoldIndicator;
    private char titleHoldPrintIndicator;
    private int pickupLocationOrganisationNumber;
    private int copyBranchNumber;
    
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
     * @return Returns the titleHoldCreationDate.
     * @exception
     * @since 1.0
     */
    public Date getTitleHoldCreationDate() {
        return titleHoldCreationDate;
    }
    /**
     * @param titleHoldCreationDate The titleHoldCreationDate to set.
     * @exception
     * @since 1.0
     */
    public void setTitleHoldCreationDate(Date titleHoldCreationDate) {
        this.titleHoldCreationDate = titleHoldCreationDate;
    }
    /**
     * @return Returns the titleHoldNumber.
     * @exception
     * @since 1.0
     */
    public int getTitleHoldNumber() {
        return titleHoldNumber;
    }
    /**
     * @param titleHoldNumber The titleHoldNumber to set.
     * @exception
     * @since 1.0
     */
    public void setTitleHoldNumber(int titleHoldNumber) {
        this.titleHoldNumber = titleHoldNumber;
    }
    /**
     * @return Returns the titleHoldPrintIndicator.
     * @exception
     * @since 1.0
     */
    public char getTitleHoldPrintIndicator() {
        return titleHoldPrintIndicator;
    }
    /**
     * @param titleHoldPrintIndicator The titleHoldPrintIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setTitleHoldPrintIndicator(char titleHoldPrintIndicator) {
        this.titleHoldPrintIndicator = titleHoldPrintIndicator;
    }
    /**
     * @return Returns the titleTimeHoldIndicator.
     * @exception
     * @since 1.0
     */
    public char getTitleTimeHoldIndicator() {
        return titleTimeHoldIndicator;
    }
    /**
     * @param titleTimeHoldIndicator The titleTimeHoldIndicator to set.
     * @exception
     * @since 1.0
     */
    public void setTitleTimeHoldIndicator(char titleTimeHoldIndicator) {
        this.titleTimeHoldIndicator = titleTimeHoldIndicator;
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
    /**
     * @return Returns the copyBranchNumber.
     * @exception
     * @since 1.0
     */
    public int getCopyBranchNumber() {
        return copyBranchNumber;
    }
    /**
     * @param copyBranchNumber The copyBranchNumber to set.
     * @exception
     * @since 1.0
     */
    public void setCopyBranchNumber(int copyBranchNumber) {
        this.copyBranchNumber = copyBranchNumber;
    }
}
