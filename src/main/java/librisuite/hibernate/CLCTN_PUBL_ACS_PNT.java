package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date ;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

public class CLCTN_PUBL_ACS_PNT implements Persistence
{		 
	private static final long serialVersionUID = -5748667651316713999L;
	
	private int collectionNumber;
	private int bibItemNumber;
	private Date creationDate;
	private Date  transactionDate;
			
	private PersistenceState persistentState = new PersistenceState();

	public CLCTN_PUBL_ACS_PNT() {
		super();
	}

	public CLCTN_PUBL_ACS_PNT(int itemNumber, int collectionNumber, Date creationDate, Date transactionDate) {
		super();
		this.bibItemNumber = itemNumber;
		this.collectionNumber = collectionNumber;
		this.creationDate = creationDate;
		this.transactionDate = transactionDate;
	}

	public int getBibItemNumber() {
		return bibItemNumber;
	}

	public int getCollectionNumber() {
		return collectionNumber;
	}

	public void setBibItemNumber(int bibNumber) {
		bibItemNumber = bibNumber;
	}

	public void setCollectionNumber(int collId) {
		collectionNumber = collId;
	}

	public void evict(Object obj) throws DataAccessException {
		persistentState.evict(obj);
	}

	public HibernateUtil getDAO() {
		return persistentState.getDAO();
	}

	public int getUpdateStatus() {
		return persistentState.getUpdateStatus();
	}

	public boolean isChanged() {
		return persistentState.isChanged();
	}

	public boolean isDeleted() {
		return persistentState.isDeleted();
	}

	public boolean isNew() {
		return persistentState.isNew();
	}

	public boolean isRemoved() {
		return persistentState.isRemoved();
	}

	public void markChanged() {
		persistentState.markChanged();
	}

	public void markDeleted() {
		persistentState.markDeleted();
	}

	public void markNew() {
		persistentState.markNew();
	}

	public void markUnchanged() {
		persistentState.markUnchanged();
	}

	public boolean onDelete(Session arg0) throws CallbackException {
		return persistentState.onDelete(arg0);
	}

	public void onLoad(Session arg0, Serializable arg1) {
		persistentState.onLoad(arg0, arg1);
	}

	public boolean onSave(Session arg0) throws CallbackException {
		return persistentState.onSave(arg0);
	}

	public boolean onUpdate(Session arg0) throws CallbackException {
		return persistentState.onUpdate(arg0);
	}

	public void setUpdateStatus(int i) {
		persistentState.setUpdateStatus(i);
	}

	public void evict() throws DataAccessException {
		evict((Object)this);
	}

	public void generateNewKey() throws DataAccessException {
		// not applicable for this class
	}

	public boolean equals(Object arg0) {
		if (arg0 instanceof CLCTN_PUBL_ACS_PNT) {
			CLCTN_PUBL_ACS_PNT c = (CLCTN_PUBL_ACS_PNT)arg0;
			return this.getBibItemNumber() == c.getBibItemNumber() &&
					this.getCollectionNumber() == c.collectionNumber;
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		return getBibItemNumber() + getCollectionNumber();
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public String getCreationDateString() 
	{
		if (getCreationDate() != null) {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			return formatter.format(getCreationDate());
		}
		else {
			return "";
		}
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public String getTransactionDateString() {
		if (getTransactionDate() != null) {
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			return formatter.format(getTransactionDate());
		}
		else {
			return "";
		}
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
}