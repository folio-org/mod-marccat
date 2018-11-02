package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOCopy;
import org.folio.marccat.dao.DAOGlobalVariable;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.model.Subfield;

import java.io.Serializable;
import java.util.Date;

public class BND_CPY implements Persistence, Serializable {

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
  private boolean barcodeAssigned = false;

  public String getBarCodeNumber() {
    return barCodeNumber;
  }

  public void setBarCodeNumber(String string) {
    barCodeNumber = string;
  }

  public int getBibItemNumber() {
    return bibItemNumber;
  }

  public void setBibItemNumber(int i) {
    bibItemNumber = i;
  }

  public int getBranchOrganisationNumber() {
    return branchOrganisationNumber;
  }

  public void setBranchOrganisationNumber(int i) {
    branchOrganisationNumber = i;
  }

  public int getCopyIdNumber() {
    return copyIdNumber;
  }

  public void setCopyIdNumber(int i) {
    copyIdNumber = i;
  }

  public String getCopyNumberDescription() {
    return copyNumberDescription;
  }

  public void setCopyNumberDescription(String string) {
    copyNumberDescription = string;
  }

  public String getCopyRemarkNote() {
    return copyRemarkNote;
  }

  public void setCopyRemarkNote(String string) {
    this.copyRemarkNote = string;
    this.copyRemarkNoteForMap = string;

//---->	Devo togliere tutti i $ + sottocampo per visualizzare il campo in mappa
    if (string != null && string.length() > 0) {
      this.copyRemarkNoteForMap = string.replaceAll(Subfield.SUBFIELD_DELIMITER + ".{1}", " ");
    }
  }

  public String getCopyRemarkNoteSortForm() {
    return copyRemarkNoteSortForm;
  }

  public void setCopyRemarkNoteSortForm(String string) {
    copyRemarkNoteSortForm = string;
  }

  public String getCopyStatementText() {
    return copyStatementText;
  }

  public void setCopyStatementText(String string) {
    this.copyStatementText = string;
    this.copyStatementTextForMap = string;

//---->	Devo togliere tutti i $ + sottocampo per visualizzare il campo in mappa
    if (string != null && string.length() > 0) {
      this.copyStatementTextForMap = string.replaceAll(Subfield.SUBFIELD_DELIMITER + ".{1}", " ");
    }
  }

  public Float getCost() {
    return cost;
  }

  public void setCost(Float float1) {
    cost = float1;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date date) {
    creationDate = date;
  }

  public Float getCurrencyExchangeRte() {
    return currencyExchangeRte;
  }

  public void setCurrencyExchangeRte(Float float1) {
    currencyExchangeRte = float1;
  }

  public Short getCurrencyTypeCode() {
    return currencyTypeCode;
  }

  public void setCurrencyTypeCode(Short short1) {
    currencyTypeCode = short1;
  }

  public Integer getDynixSerialIdNumber() {
    return dynixSerialIdNumber;
  }

  public void setDynixSerialIdNumber(Integer integer) {
    dynixSerialIdNumber = integer;
  }

  public String getHoldingAcsnListCode() {
    if (holdingAcsnListCode != null)
      return holdingAcsnListCode.trim();
    else
      return holdingAcsnListCode;
  }

  public void setHoldingAcsnListCode(String string) {
    holdingAcsnListCode = string;
  }

  public char getHoldingLevelOfDetailCode() {
    return holdingLevelOfDetailCode;
  }

  public void setHoldingLevelOfDetailCode(char holdingLevelOfDetailCode) {
    this.holdingLevelOfDetailCode = holdingLevelOfDetailCode;
  }

  public char getHoldingRetentionCode() {
    return holdingRetentionCode;
  }

  public void setHoldingRetentionCode(char holdingRetentionCode) {
    this.holdingRetentionCode = holdingRetentionCode;
  }

  public char getHoldingSeriesTrmtCode() {
    return holdingSeriesTrmtCode;
  }

  public void setHoldingSeriesTrmtCode(char holdingSeriesTrmtCode) {
    this.holdingSeriesTrmtCode = holdingSeriesTrmtCode;
  }

  public char getHoldingStatusTypeCode() {
    return holdingStatusTypeCode;
  }

  public void setHoldingStatusTypeCode(char holdingStatusTypeCode) {
    this.holdingStatusTypeCode = holdingStatusTypeCode;
  }

  public char getHoldingSubscriptionStatusCode() {
    return holdingSubscriptionStatusCode;
  }

  public void setHoldingSubscriptionStatusCode(char holdingSubscriptionStatusCode) {
    this.holdingSubscriptionStatusCode = holdingSubscriptionStatusCode;
  }

  public char getIllCode() {
    return illCode;
  }

  public void setIllCode(char illCode) {
    this.illCode = illCode;
  }

  public char getLoanPrd() {
    return loanPrd;
  }

  public void setLoanPrd(char loanPrd) {
    this.loanPrd = loanPrd;
  }

  public short getLocationNameCode() {
    return locationNameCode;
  }

  public void setLocationNameCode(short s) {
    locationNameCode = s;
  }

  public String getMaterialDescription() {
    return materialDescription;
  }

  public void setMaterialDescription(String string) {
    materialDescription = string;
  }

  public Character getMethodAdquisition() {
    return methodAdquisition;
  }

  public void setMethodAdquisition(Character c) {
    methodAdquisition = c;
  }

  public int getOrganisationNumber() {
    return organisationNumber;
  }

  public void setOrganisationNumber(int i) {
    organisationNumber = i;
  }

  public Integer getOriginalOrganisationNumber() {
    return originalOrganisationNumber;
  }

  public void setOriginalOrganisationNumber(Integer integer) {
    originalOrganisationNumber = integer;
  }

  public Integer getPhysicalCopyType() {
    return physicalCopyType;
  }

  public void setPhysicalCopyType(Integer integer) {
    physicalCopyType = integer;
  }

  public Integer getShelfListKeyNumber() {
    return shelfListKeyNumber;
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

  public Short getTempLocationNameCode() {
    return tempLocationNameCode;
  }

  public void setTempLocationNameCode(Short short1) {
    tempLocationNameCode = short1;
  }

  public Integer getTempLocationOrganizationNumber() {
    return tempLocationOrganizationNumber;
  }

  public void setTempLocationOrganizationNumber(Integer integer) {
    tempLocationOrganizationNumber = integer;
  }

  public Integer getTransferCstdyNumber() {
    return transferCstdyNumber;
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

  public void setUpdateStatus(int i) {
    persistenceState.setUpdateStatus(i);
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

  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setCopyIdNumber(dao.getNextNumber("HC", session));
    setCreationDate(new Date());
    setTransactionDate(new Date());

    if ((new DAOGlobalVariable().getValueByName("barrcode", session)).equals("1")) {
      setBarcodeAssigned(true);
      setBarCodeNumber(String.valueOf(getCopyIdNumber()));
    } else {
      setBarCodeNumber("");
    }
  }

  public void deleteNewKey(final Session session) throws DataAccessException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setCopyIdNumber(dao.getPreviouwsNumber("HC"));
    setCreationDate(new Date());
    setTransactionDate(new Date());

    if ((new DAOGlobalVariable().getValueByName("barrcode", session)).equals("1")) {
      setBarcodeAssigned(true);
      setBarCodeNumber(String.valueOf(getCopyIdNumber()));
    } else {
      setBarcodeAssigned(false);
      setBarCodeNumber("");
    }
  }

  public AbstractDAO getDAO() {
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
