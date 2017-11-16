package librisuite.hibernate;

import java.io.Serializable;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.casalini.transfer.business.DAOCasTrnsfPrdct;
import com.libricore.librisuite.common.HibernateUtil;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
public class CasTransferProd implements Persistence{

	static DAOCasTrnsfPrdct dao = new DAOCasTrnsfPrdct();
	
	
	private int bibItemNumber;
	private char booked;
	private char imported;
	private int transactionId;
	
	public CasTransferProd(int bibItemNumber) {
		this();
		setBibItemNumber(bibItemNumber);
		
	}
	
	
	public int getBibItemNumber() {
		return bibItemNumber;
	}
	public void setBibItemNumber(int bibItemNumber) {
		this.bibItemNumber = bibItemNumber;
	}
	public char getBooked() {
		return booked;
	}
	public void setBooked(char booked) {
		this.booked = booked;
	}
	public char getImported() {
		return imported;
	}
	public void setImported(char imported) {
		this.imported = imported;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	
	private PersistenceState persistenceState = new PersistenceState();
	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public CasTransferProd() {
		super();
		// TODO Auto-generated constructor stub
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
	
	/**
	 * 
	 * @since 1.0
	 */
	public HibernateUtil getDAO() {
		return dao;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}


	public void generateNewKey() throws DataAccessException {
		// TODO Auto-generated method stub
		
	}


	public boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isDeleted() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}


	public void markChanged() {
		// TODO Auto-generated method stub
		
	}


	public void markDeleted() {
		// TODO Auto-generated method stub
		
	}


	public void markNew() {
		// TODO Auto-generated method stub
		
	}


	public void markUnchanged() {
		// TODO Auto-generated method stub
		
	}


	public void setUpdateStatus(int i) {
		// TODO Auto-generated method stub
		
	}


	public boolean onDelete(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}


	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub
		
	}


	public boolean onSave(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean onUpdate(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bibItemNumber;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CasTransferProd other = (CasTransferProd) obj;
		if (bibItemNumber != other.bibItemNumber)
			return false;
		return true;
	}
}
