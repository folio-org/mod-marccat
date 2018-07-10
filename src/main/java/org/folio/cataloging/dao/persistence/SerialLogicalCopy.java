/*
 * (c) LibriCore
 * 
 * Created on Jan 25, 2005
 * 
 * SerialLogicalCopy.java
 */
package org.folio.cataloging.dao.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.HibernateException;
import org.folio.cataloging.dao.DAOSystemNextNumber;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Persistence;
import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.RecordNotFoundException;
import org.folio.cataloging.dao.ShelfListDAO;
import org.folio.cataloging.business.serialControl.SubscriptionConfigurationException;
import org.folio.cataloging.business.serialControl.SubscriptionNeedsShelfException;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/21 13:33:34 $
 * @since 1.0
 */
public class SerialLogicalCopy implements Persistence, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List/* <SerialPart> */issues = new ArrayList();
	private List deletedIssues = new ArrayList();
	private int serialCopyNumber;
	private String label;
	private int routingListNumber;
	private int peopleListNumber;
	private int bindingInstructionNumber;
	private int orderNumber;
	private String deliveryDelay = "0";
	private boolean autoClaim;
	private int orderItemNumber;
	private boolean createCopiesIndicator;
	private Integer shelfListKeyNumber;
	private Integer branchNumber;
	private Integer locationCode;
	private char loanPeriod = '2';
	private final PersistenceState persistenceState = new PersistenceState();

	private SHLF_LIST shelfList = new SHLF_LIST();
	/**
	 * Class constructor
	 * 
	 * 
	 * @since 1.0
	 */
	public SerialLogicalCopy() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.common.Persistence#evict()
	 */
	public void evict() throws DataAccessException {
		evict(this);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void evict(Object obj) throws DataAccessException {
		persistenceState.evict(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		setSerialCopyNumber(new DAOSystemNextNumber().getNextNumber("EC"));
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getBindingInstructionNumber() {
		return bindingInstructionNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public HibernateUtil getDAO() {
		return persistenceState.getDAO();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getDeliveryDelay() {
		return deliveryDelay;
	}

	/**
	 * @return the issues
	 */
	public List getIssues() {
		return issues;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getOrderItemNumber() {
		return orderItemNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getOrderNumber() {
		return orderNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getPeopleListNumber() {
		return peopleListNumber;
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
	public int getRoutingListNumber() {
		return routingListNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getSerialCopyNumber() {
		return serialCopyNumber;
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
	public boolean isAutoClaim() {
		return autoClaim;
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
	public boolean isCreateCopiesIndicator() {
		return createCopiesIndicator;
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
	public void setAutoClaim(boolean b) {
		autoClaim = b;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBindingInstructionNumber(int i) {
		bindingInstructionNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setCreateCopiesIndicator(boolean b) {
		createCopiesIndicator = b;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDeliveryDelay(String string) {
		deliveryDelay = string;
	}

	/**
	 * @param issues
	 *            the issues to set
	 */
	public void setIssues(List issues) {
		this.issues = issues;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setLabel(String string) {
		label = string;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setOrderItemNumber(int i) {
		orderItemNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setOrderNumber(int i) {
		orderNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setPeopleListNumber(int i) {
		peopleListNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setRoutingListNumber(int i) {
		routingListNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setSerialCopyNumber(int i) {
		serialCopyNumber = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUpdateStatus(int i) {
		persistenceState.setUpdateStatus(i);
	}

	public void validate() throws SubscriptionConfigurationException, SubscriptionNeedsShelfException {
		if (shelfList == null || shelfList.getShelfListKeyNumber() <= 0) {
			throw new SubscriptionNeedsShelfException();
		}
	}

	/**
	 * @return the shelfListKeyNumber
	 */
	public Integer getShelfListKeyNumber() {
		return shelfListKeyNumber;
	}

	/**
	 * @param shelfListKeyNumber the shelfListKeyNumber to set
	 */
    //TODO: The session is missing from the method
	public void setShelfListKeyNumber(Integer shelfListKeyNumber) throws HibernateException {
		this.shelfListKeyNumber = shelfListKeyNumber;
		if (shelfListKeyNumber != null && shelfListKeyNumber.intValue() > 0) {
			try {
				shelfList = new ShelfListDAO().load(shelfListKeyNumber.intValue(), null);
			} catch (RecordNotFoundException e) {
				// leave shelf unassigned
			} catch (DataAccessException e) {
				//leave shelf unassigned
			}
		}
	}

	/**
	 * @return the branchNumber
	 */
	public Integer getBranchNumber() {
		return branchNumber;
	}

	/**
	 * @param branchNumber the branchNumber to set
	 */
	public void setBranchNumber(Integer branchNumber) {
		this.branchNumber = branchNumber;
	}

	/**
	 * @return the locationCode
	 */
	public Integer getLocationCode() {
		return locationCode;
	}

	/**
	 * @param locationCode the locationCode to set
	 */
	public void setLocationCode(Integer locationCode) {
		this.locationCode = locationCode;
	}

	/**
	 * @return the loanPeriod
	 */
	public char getLoanPeriod() {
		return loanPeriod;
	}

	/**
	 * @param loanPeriod the loanPeriod to set
	 */
	public void setLoanPeriod(char loanPeriod) {
		this.loanPeriod = loanPeriod;
	}

	public Character getShelfListType() throws RecordNotFoundException, DataAccessException, HibernateException {
		Character result = null;
		if (getShelfList() != null) {
			result = new Character(getShelfList().getTypeCode());
		}
		return result;
	}

	public String getShelfListNumber() throws RecordNotFoundException, DataAccessException, HibernateException {
		String result = null;
		if (getShelfList() != null) {
			result = getShelfList().getDisplayText();
		}
		return result;
	}
	/**
	 * @return the shelfList
	 * @throws DataAccessException 
	 * @throws RecordNotFoundException 
	 */
	//TODO The session is missing from the method
	public SHLF_LIST getShelfList() throws RecordNotFoundException, DataAccessException, HibernateException {
		if (shelfList == null && shelfListKeyNumber != null) {
			shelfList = (SHLF_LIST)new ShelfListDAO().load(shelfListKeyNumber.intValue(), null);
		}
		return shelfList;
	}

	/**
	 * @param shelfList the shelfList to set
	 */
	public void setShelfList(SHLF_LIST shelfList) throws HibernateException {
		this.shelfList = shelfList;
		if (shelfList != null) {
			setShelfListKeyNumber(new Integer(shelfList.getShelfListKeyNumber()));
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 instanceof SerialLogicalCopy) {
			SerialLogicalCopy o = (SerialLogicalCopy)arg0;
			if (o.getSerialCopyNumber() > 0) {
				return o.getSerialCopyNumber() == this.getSerialCopyNumber();
			}
			else {
				return o == this;
			}
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.getSerialCopyNumber();
	}

	public void deleteIssue(Integer index) {
		if (index == null) {
			return;
		}
		SerialPart issue = (SerialPart)getIssues().get(index.intValue());
		if (!issue.isNew()) {
			getDeletedIssues().add(issue);
			issue.markDeleted();
		}
		
		getIssues().remove(index.intValue());
		
	}
	
	public void deleteSingleIssue(Integer index) {
		if (index == null) {
			return;
		}
		SerialPart issue = (SerialPart)getIssues().get(index.intValue());
		if (!issue.isNew()) {
			getDeletedIssues().add(issue);
			issue.markDeleted();
		}
	
	}

	/**
	 * @return the deletedIssues
	 */
	public List getDeletedIssues() {
		return deletedIssues;
	}

	/**
	 * @param deletedIssues the deletedIssues to set
	 */
	public void setDeletedIssues(List deletedIssues) {
		this.deletedIssues = deletedIssues;
	}
}
