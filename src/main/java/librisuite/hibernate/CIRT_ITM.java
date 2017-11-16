/*
 * (c) LibriCore
 * 
 * Created on 20-jul-2004
 * 
 * CIRT_ITM.java
 */
package librisuite.hibernate;

import java.util.Date;

/**
 * @author elena
 * @version $Revision: 1.4 $, $Date: 2004/08/04 12:44:20 $
 * @since 1.0
 */
public class CIRT_ITM {
	private int copyIdNumber;
	private int organisationNumber;
	private int bibItemNumber;
	private int personNumber;
	private String barCodeNumber;
	private short circulationStatesticsTypeCode;
	private Date circulationItemChargeOutDate;
	private Date circulationItemDueDate;
	private Date circulationItemPreviousDueDate;
	private Date circulationItemRecallNotificationDate;
	private Date circulationItemFirstOverDate;
	private Date circulationItemSecondOverDate;
	private Date circulationItemThirdOverDate;
	private int circulationItemRenewalCenterCount;
	private char circulationItemExtendLoanIndex;
	private char circulationItemIllIndex;
	private Character circulationItemTranstationIndex;
	private Integer circulationItemChekingBranch;

	public String getBarCodeNumber() {
		return barCodeNumber;
	}

	public int getBibItemNumber() {
		return bibItemNumber;
	}

	public Date getCirculationItemChargeOutDate() {
		return circulationItemChargeOutDate;
	}

	public Integer getCirculationItemChekingBranch() {
		return circulationItemChekingBranch;
	}

	public Date getCirculationItemDueDate() {
		return circulationItemDueDate;
	}

	public char getCirculationItemExtendLoanIndex() {
		return circulationItemExtendLoanIndex;
	}

	public Date getCirculationItemFirstOverDate() {
		return circulationItemFirstOverDate;
	}

	public char getCirculationItemIllIndex() {
		return circulationItemIllIndex;
	}

	public Date getCirculationItemPreviousDueDate() {
		return circulationItemPreviousDueDate;
	}

	public Date getCirculationItemRecallNotificationDate() {
		return circulationItemRecallNotificationDate;
	}

	public int getCirculationItemRenewalCenterCount() {
		return circulationItemRenewalCenterCount;
	}

	public Date getCirculationItemSecondOverDate() {
		return circulationItemSecondOverDate;
	}

	public Date getCirculationItemThirdOverDate() {
		return circulationItemThirdOverDate;
	}

	public Character getCirculationItemTranstationIndex() {
		return circulationItemTranstationIndex;
	}

	public short getCirculationStatesticsTypeCode() {
		return circulationStatesticsTypeCode;
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

	public void setBarCodeNumber(String string) {
		barCodeNumber = string;
	}

	public void setBibItemNumber(int i) {
		bibItemNumber = i;
	}

	public void setCirculationItemChargeOutDate(Date date) {
		circulationItemChargeOutDate = date;
	}

	public void setCirculationItemChekingBranch(Integer integer) {
		circulationItemChekingBranch = integer;
	}

	public void setCirculationItemDueDate(Date date) {
		circulationItemDueDate = date;
	}

	public void setCirculationItemExtendLoanIndex(char c) {
		circulationItemExtendLoanIndex = c;
	}

	public void setCirculationItemFirstOverDate(Date date) {
		circulationItemFirstOverDate = date;
	}

	public void setCirculationItemIllIndex(char c) {
		circulationItemIllIndex = c;
	}

	public void setCirculationItemPreviousDueDate(Date date) {
		circulationItemPreviousDueDate = date;
	}

	public void setCirculationItemRecallNotificationDate(Date date) {
		circulationItemRecallNotificationDate = date;
	}

	public void setCirculationItemRenewalCenterCount(int i) {
		circulationItemRenewalCenterCount = i;
	}

	public void setCirculationItemSecondOverDate(Date date) {
		circulationItemSecondOverDate = date;
	}

	public void setCirculationItemThirdOverDate(Date date) {
		circulationItemThirdOverDate = date;
	}

	public void setCirculationItemTranstationIndex(Character c) {
		circulationItemTranstationIndex = c;
	}

	public void setCirculationStatesticsTypeCode(short s) {
		circulationStatesticsTypeCode = s;
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

}
