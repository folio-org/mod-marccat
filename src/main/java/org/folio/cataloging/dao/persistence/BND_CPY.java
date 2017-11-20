package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.Date;

import org.folio.cataloging.dao.DAOCopy;
import org.folio.cataloging.dao.DAOGlobalVariable;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.common.HibernateUtil;
import org.folio.cataloging.model.Subfield;

public class BND_CPY implements Persistence, Serializable 
{

	private static final long serialVersionUID = 4286339906738625939L;
	private int copyIdNumber;
	private int bibItemNumber;
	private Integer shelfListKeyNumber;
//	private SHLF_LIST shelfList;
	private int organisationNumber;
	private int branchOrganisationNumber;
	private Integer originalOrganisationNumber;
	private String barCodeNumber;
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
	private String copyRemarkNoteForMap;
	private String copyStatementTextForMap;
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
	private boolean barcodeAssigned=false;

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

	public String getHoldingAcsnListCode() 
	{
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

	public void setCopyRemarkNote(String string) 
	{
		this.copyRemarkNote = string;
		this.copyRemarkNoteForMap = string;
		
//---->	Devo togliere tutti i $ + sottocampo per visualizzare il campo in mappa
		if (string !=null && string.length()>0){
			this.copyRemarkNoteForMap = string.replaceAll(Subfield.SUBFIELD_DELIMITER + ".{1}", " ");
		}
	}

	public void setCopyRemarkNoteSortForm(String string) {
		copyRemarkNoteSortForm = string;
	}

	public void setCopyStatementText(String string) 
	{
		this.copyStatementText = string;
		this.copyStatementTextForMap = string;
		
//---->	Devo togliere tutti i $ + sottocampo per visualizzare il campo in mappa
		if (string !=null && string.length()>0){
			this.copyStatementTextForMap = string.replaceAll(Subfield.SUBFIELD_DELIMITER + ".{1}", " ");
		}
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

	public void setHoldingSubscriptionStatusCode(char holdingSubscriptionStatusCode) {
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

	public void setShelfListKeyNumber(Integer integer) {
		/*
		 * The AMICUS database, for historic reasons?, has rows with both 0 and null in this
		 * column -- both should be treated as null
		 */
		if (integer != null && integer.intValue() == 0) {
			shelfListKeyNumber = null;
		} else {
			shelfListKeyNumber = integer;
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

//	public SHLF_LIST getShelfList() {
//		return shelfList;
//	}
//
//	public void setShelfList(SHLF_LIST shlf_list) {
//		shelfList = shlf_list;
//	}

	public PersistenceState getPersistenceState() {
		return persistenceState;
	}

	public void setPersistenceState(PersistenceState state) {
		persistenceState = state;
	}

	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}
	
	public void evict() throws DataAccessException {
		persistenceState.evict(this);
	}

	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	public boolean isChanged() {
		return persistenceState.isChanged();
	}

	public boolean isDeleted() {
		return persistenceState.isDeleted();
	}

	public boolean isNew() {
		return persistenceState.isNew();
	}

	public boolean isRemoved() {
		return persistenceState.isRemoved();
	}

	public void markChanged() {
		persistenceState.markChanged();
	}

	public void markDeleted() {
		persistenceState.markDeleted();
	}

	public void markNew() {
		persistenceState.markNew();
	}

	public void markUnchanged() {
		persistenceState.markUnchanged();
	}

	public boolean onDelete(Session arg0) throws CallbackException {
		return persistenceState.onDelete(arg0);
	}

	public void onLoad(Session arg0, Serializable arg1) {
		persistenceState.onLoad(arg0, arg1);
	}

	public boolean onSave(Session arg0) throws CallbackException {
		return persistenceState.onSave(arg0);
	}

	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistenceState.onUpdate(arg0);
	}

	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	public void generateNewKey() throws DataAccessException {
		DAOSystemNextNumber dao = new DAOSystemNextNumber();
		setCopyIdNumber(dao.getNextNumber("HC"));
		setCreationDate(new Date());
		setTransactionDate(new Date());
		
		if ((new DAOGlobalVariable().getValueByName("barrcode")).equals("1")) {
			setBarcodeAssigned(true);
			setBarCodeNumber(String.valueOf(getCopyIdNumber()));
		}
		else {
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
		}
		else {
			setBarcodeAssigned(false);
			setBarCodeNumber("");
		}
	}

	public HibernateUtil getDAO() {
		return new DAOCopy();
	}

	public boolean isBarcodeAssigned() {
		return barcodeAssigned;
	}

	public void setBarcodeAssigned(boolean barcodeAssigned) {
		this.barcodeAssigned = barcodeAssigned;
	}

	public String getCopyRemarkNoteForMap() {
		return copyRemarkNoteForMap;
	}

	public void setCopyRemarkNoteForMap(String copyRemarkNoteForMap) {
		this.copyRemarkNoteForMap = copyRemarkNoteForMap;
	}

	public String getCopyStatementTextForMap() {
		return copyStatementTextForMap;
	}

	public void setCopyStatementTextForMap(String copyStatementTextForMap) {
		this.copyStatementTextForMap = copyStatementTextForMap;
	}
}