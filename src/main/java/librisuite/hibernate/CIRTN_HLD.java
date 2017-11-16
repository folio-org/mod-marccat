/*
 * (c) LibriCore
 * 
 * Created on 26-jul-2004
 * 
 * CIRTN_HLD.java
 */
package librisuite.hibernate;

import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.3 $, $Date: 2004/08/04 09:02:38 $
 * @since 1.0
 */
public class CIRTN_HLD {

	private int copyIdNumber;
	private int organisationNumber;
	private int bibItemNumber;
	private int personNumber;
	private int circulationHoldingLocationOrganisationNumber;
	private short circulationHoldingTypeCode;
	private Date circulationHoldingCreationDate;
	private Date circulationHolginShelfDate;
	private Date timeHoldingStratmentDate;
	private Date timeHoldingEndDate;
	private Character timeHoldingStatusIndex;
	private Character circulationHoldingPrintIndex;
	private int branchOrganisationNumber;

	public int getBibItemNumber() {
		return bibItemNumber;
	}

	public int getBranchOrganisationNumber() {
		return branchOrganisationNumber;
	}

	public Date getCirculationHoldingCreationDate() {
		return circulationHoldingCreationDate;
	}

	public int getCirculationHoldingLocationOrganisationNumber() {
		return circulationHoldingLocationOrganisationNumber;
	}

	public Character getCirculationHoldingPrintIndex() {
		return circulationHoldingPrintIndex;
	}

	public short getCirculationHoldingTypeCode() {
		return circulationHoldingTypeCode;
	}

	public Date getCirculationHolginShelfDate() {
		return circulationHolginShelfDate;
	}

	public int getCopyIdNumber() {
		return copyIdNumber;
	}

	public int getOrganisationNumber() {
		return organisationNumber;
	}

	public int getPersonNumber() {
		return personNumber;
	}

	public Date getTimeHoldingEndDate() {
		return timeHoldingEndDate;
	}

	public Character getTimeHoldingStatusIndex() {
		return timeHoldingStatusIndex;
	}

	public Date getTimeHoldingStratmentDate() {
		return timeHoldingStratmentDate;
	}

	public void setBibItemNumber(int i) {
		bibItemNumber = i;
	}

	public void setBranchOrganisationNumber(int i) {
		branchOrganisationNumber = i;
	}

	public void setCirculationHoldingCreationDate(Date date) {
		circulationHoldingCreationDate = date;
	}

	public void setCirculationHoldingLocationOrganisationNumber(int i) {
		circulationHoldingLocationOrganisationNumber = i;
	}

	public void setCirculationHoldingPrintIndex(Character character) {
		circulationHoldingPrintIndex = character;
	}

	public void setCirculationHoldingTypeCode(short s) {
		circulationHoldingTypeCode = s;
	}

	public void setCirculationHolginShelfDate(Date date) {
		circulationHolginShelfDate = date;
	}

	public void setCopyIdNumber(int i) {
		copyIdNumber = i;
	}

	public void setOrganisationNumber(int i) {
		organisationNumber = i;
	}

	public void setPersonNumber(int i) {
		personNumber = i;
	}

	public void setTimeHoldingEndDate(Date date) {
		timeHoldingEndDate = date;
	}

	public void setTimeHoldingStatusIndex(Character character) {
		timeHoldingStatusIndex = character;
	}

	public void setTimeHoldingStratmentDate(Date date) {
		timeHoldingStratmentDate = date;
	}

}
