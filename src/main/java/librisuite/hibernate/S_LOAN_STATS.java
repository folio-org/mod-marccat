/*
 * (c) LibriCore
 * 
 * Created on 02-nov-2004
 * 
 * S_LOAN_STATS.java
 */
package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Maite
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:33 $
 * @since 1.0
 */
public class S_LOAN_STATS implements Serializable {

    private int loanStatisticsNumber;
    private int organisationNumber;
    private Date loanStatisticsTimestamp;
	private Character loanPeriodCode;
	private short borrowerTypeCode;
	private short personLocal1stTypeCode; 
	private short personLocal2ndTypeCode; 
	private short personLocal3rdTypeCode; 
	private short personLocal4thTypeCode;
	private short circulationStatisticsTypeCode;
	private Integer borrowerLoanDrtnCount;
	private String borrowerBarcodeNumber;
	private String barcodeNumber;


    public String getBarcodeNumber() {
        return barcodeNumber;
    }
    public void setBarcodeNumber(String barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }
    public String getBorrowerBarcodeNumber() {
        return borrowerBarcodeNumber;
    }
    public void setBorrowerBarcodeNumber(String borrowerBarcodeNumber) {
        this.borrowerBarcodeNumber = borrowerBarcodeNumber;
    }
    public Integer getBorrowerLoanDrtnCount() {
        return borrowerLoanDrtnCount;
    }
    public void setBorrowerLoanDrtnCount(Integer borrowerLoanDrtnCount) {
        this.borrowerLoanDrtnCount = borrowerLoanDrtnCount;
    }
    public short getBorrowerTypeCode() {
        return borrowerTypeCode;
    }
    public void setBorrowerTypeCode(short borrowerTypeCode) {
        this.borrowerTypeCode = borrowerTypeCode;
    }
    public short getCirculationStatisticsTypeCode() {
        return circulationStatisticsTypeCode;
    }
    public void setCirculationStatisticsTypeCode(
            short circulationStatisticsTypeCode) {
        this.circulationStatisticsTypeCode = circulationStatisticsTypeCode;
    }
    public Character getLoanPeriodCode() {
        return loanPeriodCode;
    }
    public void setLoanPeriodCode(Character loanPeriodCode) {
        this.loanPeriodCode = loanPeriodCode;
    }
    public int getLoanStatisticsNumber() {
        return loanStatisticsNumber;
    }
    public void setLoanStatisticsNumber(int loanStatisticsNumber) {
        this.loanStatisticsNumber = loanStatisticsNumber;
    }
    public Date getLoanStatisticsTimestamp() {
        return loanStatisticsTimestamp;
    }
    public void setLoanStatisticsTimestamp(Date loanStatisticsTimestamp) {
        this.loanStatisticsTimestamp = loanStatisticsTimestamp;
    }
    public int getOrganisationNumber() {
        return organisationNumber;
    }
    public void setOrganisationNumber(int organisationNumber) {
        this.organisationNumber = organisationNumber;
    }
    public short getPersonLocal1stTypeCode() {
        return personLocal1stTypeCode;
    }
    public void setPersonLocal1stTypeCode(short personLocal1stTypeCode) {
        this.personLocal1stTypeCode = personLocal1stTypeCode;
    }
    public short getPersonLocal2ndTypeCode() {
        return personLocal2ndTypeCode;
    }
    public void setPersonLocal2ndTypeCode(short personLocal2ndTypeCode) {
        this.personLocal2ndTypeCode = personLocal2ndTypeCode;
    }
    public short getPersonLocal3rdTypeCode() {
        return personLocal3rdTypeCode;
    }
    public void setPersonLocal3rdTypeCode(short personLocal3rdTypeCode) {
        this.personLocal3rdTypeCode = personLocal3rdTypeCode;
    }
    public short getPersonLocal4thTypeCode() {
        return personLocal4thTypeCode;
    }
    public void setPersonLocal4thTypeCode(short personLocal4thTypeCode) {
        this.personLocal4thTypeCode = personLocal4thTypeCode;
    }
}
