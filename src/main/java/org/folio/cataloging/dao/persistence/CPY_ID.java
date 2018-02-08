/*
 * (c) LibriCore
 * 
 * Created on 18-jun-2004
 * 
 * Table_CPY_ID.java
 */
package org.folio.cataloging.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.descriptor.SortFormParameters;
import org.folio.cataloging.dao.DAOCopy;
import org.folio.cataloging.dao.DAOGlobalVariable;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.exception.EmptySubfieldException;
import org.folio.cataloging.exception.InvalidShelfListTypeException;
import org.folio.cataloging.exception.NoSubfieldCodeException;
import org.folio.cataloging.exception.ValidationException;
import org.folio.cataloging.model.Subfield;
import org.folio.cataloging.util.StringText;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

/**
 * @author elena
 * @version $Revision: 1.18 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class CPY_ID implements Persistence, Serializable {
	private static final Log logger = LogFactory.getLog(CPY_ID.class);

	private int copyIdNumber;
	private int bibItemNumber;
	private Integer shelfListKeyNumber;
	private SHLF_LIST shelfList;
	private int organisationNumber;
	private int branchOrganisationNumber;
	private Integer originalOrganisationNumber;
	private String barCodeNumber;
	// TODO is not used any more
	private Integer dynixSerialIdNumber;
	private Date transactionDate;
	private Date creationDate;
	private char illCode;
	private char holdingSubscriptionStatusCode;
	private char holdingRetentionCode;
	private char loanPrd;
	private char holdingSeriesTrmtCode;
	private char holdingStatusTypeCode;
	private short locationNameCode;
	private char holdingLevelOfDetailCode;
	private String holdingAcsnListCode;
	private String copyNumberDescription;
	private String copyRemarkNote;
	private String copyStatementText;
	private String copyRemarkNoteSortForm;
	private Integer tempLocationOrganizationNumber;
	private Short tempLocationNameCode;
	private String materialDescription;
	private Float cost;
	private Short currencyTypeCode;
	private Float currencyExchangeRte;
	private Integer transferCstdyNumber;
	private Integer physicalCopyType;
	private Character methodAdquisition = new Character(' ');
	private PersistenceState persistenceState = new PersistenceState();

	private boolean barcodeAssigned = false;

	public String getBarCodeNumber() {
		return barCodeNumber;
	}

	public int getBibItemNumber() {
		return bibItemNumber;
	}

	public int getBranchOrganisationNumber() {
		return branchOrganisationNumber;
	}

	public int getCopyIdNumber() {
		return copyIdNumber;
	}

	public String getCopyNumberDescription() {
		return copyNumberDescription;
	}

	public String getCopyRemarkNote() {
		return copyRemarkNote;
	}

	public String getCopyRemarkNoteSortForm() {
		return copyRemarkNoteSortForm;
	}

	public String getCopyStatementText() {
		return copyStatementText;
	}

	public Float getCost() {
		return cost;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Float getCurrencyExchangeRte() {
		return currencyExchangeRte;
	}

	public Short getCurrencyTypeCode() {
		return currencyTypeCode;
	}

	public Integer getDynixSerialIdNumber() {
		return dynixSerialIdNumber;
	}

	public String getHoldingAcsnListCode() {
		if (holdingAcsnListCode != null)
			return holdingAcsnListCode.trim();
		else
			return holdingAcsnListCode;
	}

	public char getHoldingLevelOfDetailCode() {
		return holdingLevelOfDetailCode;
	}

	public char getHoldingRetentionCode() {
		return holdingRetentionCode;
	}

	public char getHoldingSeriesTrmtCode() {
		return holdingSeriesTrmtCode;
	}

	public char getHoldingStatusTypeCode() {
		return holdingStatusTypeCode;
	}

	public char getHoldingSubscriptionStatusCode() {
		return holdingSubscriptionStatusCode;
	}

	public char getIllCode() {
		return illCode;
	}

	public char getLoanPrd() {
		return loanPrd;
	}

	public short getLocationNameCode() {
		return locationNameCode;
	}

	public String getMaterialDescription() {
		return materialDescription;
	}

	public Character getMethodAdquisition() {
		return methodAdquisition;
	}

	public int getOrganisationNumber() {
		return organisationNumber;
	}

	public Integer getOriginalOrganisationNumber() {
		return originalOrganisationNumber;
	}

	public Integer getPhysicalCopyType() {
		return physicalCopyType;
	}

	public Integer getShelfListKeyNumber() {
		return shelfListKeyNumber;
	}

	public Short getTempLocationNameCode() {
		return tempLocationNameCode;
	}

	public Integer getTempLocationOrganizationNumber() {
		return tempLocationOrganizationNumber;
	}

	public Integer getTransferCstdyNumber() {
		return transferCstdyNumber;
	}

	public void setBarCodeNumber(String string) {
		barCodeNumber = string;
	}

	public void setBibItemNumber(int i) {
		bibItemNumber = i;
	}

	public void setBranchOrganisationNumber(int i) {
		branchOrganisationNumber = i;
	}

	public void setCopyIdNumber(int i) {
		copyIdNumber = i;
	}

	public void setCopyNumberDescription(String string) {
		copyNumberDescription = string;
	}

	public void setCopyRemarkNote(String s) {
		this.copyRemarkNote = s;
		try {
			this.copyRemarkNoteSortForm = calculateSortForm(s);
		} catch (Exception e) {
			// ignore sortform exception for copy remark note
			logger.warn("sortform creation failed for copyRemarkNote");
		}
	}

	public void setCopyRemarkNoteSortForm(String string) {
		copyRemarkNoteSortForm = string;
	}

	public void setCopyStatementText(String copyStatementText) {
		this.copyStatementText = copyStatementText;
	}

	public void setCost(Float float1) {
		cost = float1;
	}

	public void setCreationDate(Date date) {
		creationDate = date;
	}

	public void setCurrencyExchangeRte(Float float1) {
		currencyExchangeRte = float1;
	}

	public void setCurrencyTypeCode(Short short1) {
		currencyTypeCode = short1;
	}

	public void setDynixSerialIdNumber(Integer integer) {
		dynixSerialIdNumber = integer;
	}

	public void setHoldingAcsnListCode(String string) {
		holdingAcsnListCode = string;
	}

	public void setHoldingLevelOfDetailCode(char holdingLevelOfDetailCode) {
		this.holdingLevelOfDetailCode = holdingLevelOfDetailCode;
	}

	public void setHoldingRetentionCode(char holdingRetentionCode) {
		this.holdingRetentionCode = holdingRetentionCode;
	}

	public void setHoldingSeriesTrmtCode(char holdingSeriesTrmtCode) {
		this.holdingSeriesTrmtCode = holdingSeriesTrmtCode;
	}

	public void setHoldingStatusTypeCode(char holdingStatusTypeCode) {
		this.holdingStatusTypeCode = holdingStatusTypeCode;
	}

	public void setHoldingSubscriptionStatusCode(
			char holdingSubscriptionStatusCode) {
		this.holdingSubscriptionStatusCode = holdingSubscriptionStatusCode;
	}

	public void setIllCode(char illCode) {
		this.illCode = illCode;
	}

	public void setLoanPrd(char loanPrd) {
		this.loanPrd = loanPrd;
	}

	public void setLocationNameCode(short s) {
		locationNameCode = s;
	}

	public void setMaterialDescription(String string) {
		materialDescription = string;
	}

	public void setMethodAdquisition(Character c) {
		methodAdquisition = c;
	}

	public void setOrganisationNumber(int i) {
		organisationNumber = i;
	}

	public void setOriginalOrganisationNumber(Integer integer) {
		originalOrganisationNumber = integer;
	}

	public void setPhysicalCopyType(Integer integer) {
		physicalCopyType = integer;
	}

	public void setShelfListKeyNumber(Integer shelfListKeyNumber) {
		/*
		 * The AMICUS database, for historic reasons?, has rows with both 0 and
		 * null in this column -- both should be treated as null
		 */
		if ((shelfListKeyNumber != null) && (shelfListKeyNumber == 0)) {
			this.shelfListKeyNumber = null;
		} else {
			this.shelfListKeyNumber = shelfListKeyNumber;
		}
	}

	public void setTempLocationNameCode(Short short1) {
		tempLocationNameCode = short1;
	}

	public void setTempLocationOrganizationNumber(Integer integer) {
		tempLocationOrganizationNumber = integer;
	}

	public void setTransferCstdyNumber(Integer integer) {
		transferCstdyNumber = integer;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date date) {
		transactionDate = date;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public SHLF_LIST getShelfList() {
		return shelfList;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setShelfList(SHLF_LIST shlf_list) {
		shelfList = shlf_list;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public PersistenceState getPersistenceState() {
		return persistenceState;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPersistenceState(PersistenceState state) {
		persistenceState = state;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isNew() {
		return persistenceState.isNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markChanged() {
		persistenceState.markChanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markDeleted() {
		persistenceState.markDeleted();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markNew() {
		persistenceState.markNew();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0, arg1);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	/**
	 * Performs validation of this object to ensure that it is ready to be saved
	 * to the database. Throws exceptions for failed validations
	 */
	public void validate() throws
            ValidationException {

		/*if (getTransactionDate() != null
				&& getTransactionDate().before(getCreationDate())) {
			throw new InvalidTransactionDateException();
		}*/

		StringText remark = new StringText(getCopyRemarkNote());
		Iterator iter = remark.getSubfieldList().iterator();
		while (iter.hasNext()) {
			Subfield s = (Subfield) iter.next();
			if (s.isEmpty()) {
				throw new EmptySubfieldException();
			}
		}

		String stmt = getCopyStatementText();
		if (stmt != null && stmt.length() > 0) {
			StringText s = new StringText(stmt);
			if (s.isEmpty()) {
				throw new NoSubfieldCodeException();
			}

			iter = s.getSubfieldList().iterator();
			while (iter.hasNext()) {
				if (((Subfield) iter.next()).isEmpty()) {
					throw new EmptySubfieldException();
				}
			}
			StringText invalid = s
					.getSubfieldsWithoutCodes("abceghjiklmnpqstxz");
			if (invalid.getNumberOfSubfields() > 0) {
				throw new ValidationException();
			}
		}

		if (getShelfList() != null && getShelfList().getTypeCode() == '?') {
			throw new InvalidShelfListTypeException();
		}
	}

	public String calculateSortForm(String s) throws
            DataAccessException {
		String sortForm = "";
		if (s != null && s.length() > 0) {
			SortFormParameters parms = new SortFormParameters(100, 105, 0, 0, 0);
			sortForm = new DAOCopy().calculateSortForm(s, parms);
		}
		return sortForm;
	}
	/**
	 * Convenience method for displaying copyRemarkNote as a text string.
	 * The expression ForMap -- refers to "for the jsp page(s)"
	 * @return
	 */
	public String getCopyRemarkNoteForMap() {
		String result = new StringText(getCopyRemarkNote()).getMarcDisplayString();
		logger.debug("Converting '" + getCopyRemarkNote() + "' to '" + result + "'");
		return result;
	}
	/**
	 * Convenience method for displaying copyStatementText as a text string.
	 * The expression ForMap -- refers to "for the jsp page(s)"
	 * @return
	 */
	public String getCopyStatementTextForMap() {
		return new StringText(getCopyStatementText()).getMarcDisplayString();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setCopyIdNumber(dao.getNextNumber("HC"));
		Date createTime = new Date();
		setCreationDate(createTime);
		setTransactionDate(createTime);
	
		if ((new DAOGlobalVariable().getValueByName("barrcode")).equals("1")) {
			setBarcodeAssigned(true);
			setBarCodeNumber(String.valueOf(getCopyIdNumber()));
		} else {
			setBarCodeNumber("");
		}
	}


	public void deleteNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setCopyIdNumber(dao.getPreviouwsNumber("HC"));
		setCreationDate(new Date());
		setTransactionDate(new Date());

		if ((new DAOGlobalVariable().getValueByName("barrcode")).equals("1")) {
			setBarcodeAssigned(true);
			setBarCodeNumber(String.valueOf(getCopyIdNumber()));
		} else {
			setBarcodeAssigned(false);
			setBarCodeNumber("");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return new DAOCopy();
	}


	public boolean isBarcodeAssigned() {
		return barcodeAssigned;
	}

	public void setBarcodeAssigned(boolean barcodeAssigned) {
		this.barcodeAssigned = barcodeAssigned;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + copyIdNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CPY_ID other = (CPY_ID) obj;
        return copyIdNumber == other.copyIdNumber;
    }
}