package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.casalini.cataloguing.business.DAOCollectionMaster;
import com.libricore.librisuite.common.HibernateUtil;

public class CollectionMaster implements Persistence 
{
	private static final long serialVersionUID = 2522128570785338271L;

	static DAOCollectionMaster dao = new DAOCollectionMaster();
	public ArrayList clientList = new ArrayList();
	private int idCollection;
	private int nameIta;
	//private String nameEng;
    private int statusCode;
	private Date dateCreation;
	private Date dateCancel;
	private String typologyCode;
	private String userCreate;
	private String userModify;
	private String customerId;
	private Date dateModify;
//	20101015 inizio: aggiunto campo Anno
	private int year;
//	20110131 inizio: aggiunte date validita'
	private Date dateIniVal;
	private Date dateFinVal;
	
	public Date getDateIniVal() {
		return dateIniVal;
	}

	public void setDateIniVal(Date dateIniVal) {
		this.dateIniVal = dateIniVal;
	}

	public Date getDateFinVal() {
		return dateFinVal;
	}

	public void setDateFinVal(Date dateFinVal) {
		this.dateFinVal = dateFinVal;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public ArrayList getClientList() {
		return clientList;
	}
	
	public void setClientList(ArrayList clientList) {
		this.clientList = clientList;
	}
	
	public Date getDateModify() {
		return dateModify;
	}

	public void setDateModify(Date dateModify) {
		this.dateModify = dateModify;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}

	public String getUserModify() {
		return userModify;
	}

	public void setUserModify(String userModify) {
		this.userModify = userModify;
	}

	public int getIdCollection() {
		return idCollection;
	}

	public void setIdCollection(int idCollection) {
		this.idCollection = idCollection;
	}

	public int getNameIta() {
		return nameIta;
	}

	public void setNameIta(int nameIta) {
		this.nameIta = nameIta;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateCancel() {
		return dateCancel;
	}

	public void setDateCancel(Date dateCancel) {
		this.dateCancel = dateCancel;
	}

	public String getTypologyCode() {
		return typologyCode;
	}

	public void setTypologyCode(String typologyCode) {
		this.typologyCode = typologyCode;
	}

	private PersistenceState persistenceState = new PersistenceState();
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CollectionMaster() {
		super();
		//setIdCollection(idCollection);
		// TODO Auto-generated constructor stub
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
		evict((Object)this);
	}
	
	public HibernateUtil getDAO() {
		return dao;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idCollection;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollectionMaster other = (CollectionMaster) obj;
		if (idCollection != other.idCollection)
			return false;
		return true;
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
		// not applicable for this class

	}
}