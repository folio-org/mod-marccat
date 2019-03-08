/*
 * (c) LibriCore
 *
 * Created on 18-jun-2004
 *
 * Table_CPY_ID.java
 */
package org.folio.marccat.dao.persistence;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.folio.marccat.business.common.Persistence;
import org.folio.marccat.business.common.PersistenceState;
import org.folio.marccat.business.descriptor.SortFormParameters;
import org.folio.marccat.dao.AbstractDAO;
import org.folio.marccat.dao.DAOCopy;
import org.folio.marccat.dao.DAOGlobalVariable;
import org.folio.marccat.dao.SystemNextNumberDAO;
import org.folio.marccat.exception.*;
import org.folio.marccat.model.Subfield;
import org.folio.marccat.util.StringText;

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
  private Character methodAdquisition = (' ');
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

  public void setCopyRemarkNote(String s) {
    this.copyRemarkNote = s;
    try {
      this.copyRemarkNoteSortForm = calculateSortForm(s);
    } catch (Exception e) {
      // ignore sortform exception for copy remark note
      logger.warn("sortform creation failed for copyRemarkNote");
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

  public void setCopyStatementText(String copyStatementText) {
    this.copyStatementText = copyStatementText;
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

  public void setHoldingSubscriptionStatusCode(
    char holdingSubscriptionStatusCode) {
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


  public SHLF_LIST getShelfList() {
    return shelfList;
  }


  public void setShelfList(SHLF_LIST shlf_list) {
    shelfList = shlf_list;
  }


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
   *
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
   *
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
  public void generateNewKey(final Session session) throws DataAccessException, HibernateException {
    SystemNextNumberDAO dao = new SystemNextNumberDAO();
    setCopyIdNumber(dao.getNextNumber("HC", session));
    Date createTime = new Date();
    setCreationDate(createTime);
    setTransactionDate(createTime);

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

  /*
   * (non-Javadoc)
   *
   * @see librisuite.business.common.Persistence#getDAO()
   */
  public AbstractDAO getDAO() {
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
