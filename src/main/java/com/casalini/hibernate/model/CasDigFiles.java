package com.casalini.hibernate.model;

import java.io.Serializable;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.casalini.digital.business.DAOCasDigFiles;
import com.libricore.librisuite.common.HibernateUtil;

public class CasDigFiles implements Persistence 
{
	private static final long serialVersionUID = 2522128570785338271L;

	static DAOCasDigFiles dao = new DAOCasDigFiles(); 
	
	private int bibItemNumberMadre;
	private int bibItemNumberFiglia;
	private int idItem;
	private String digLevel;
	private Integer orderProgr;
	private int cntlKeyNbr;
	private String note;
	private String progressiveType;
//	20110201 inizio: aggiunto anno
	private Integer year;
//	20110201 fine
	
	public CasDigFiles(int bibItemNumberFiglia) {
		this();
		setBibItemNumberFiglia(bibItemNumberFiglia);
	}

	private PersistenceState persistenceState = new PersistenceState();
	
	/**
	 * Class constructor
	 */
	public CasDigFiles() {
		super();
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

	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + bibItemNumberFiglia;
		result = prime * result + idItem;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CasDigFiles other = (CasDigFiles) obj;
//		if (bibItemNumberFiglia != other.bibItemNumberFiglia)
		if (idItem != other.idItem)
			return false;
		return true;
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

	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#generateNewKey()
	 */
	public void generateNewKey() throws DataAccessException {
		// not applicable for this class
	}

	public int getBibItemNumberMadre() {
		return bibItemNumberMadre;
	}

	public void setBibItemNumberMadre(int bibItemNumberMadre) {
		this.bibItemNumberMadre = bibItemNumberMadre;
	}

	public int getBibItemNumberFiglia() {
		return bibItemNumberFiglia;
	}

	public void setBibItemNumberFiglia(int bibItemNumberFiglia) {
		this.bibItemNumberFiglia = bibItemNumberFiglia;
	}

	public int getIdItem() {
		return idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public Integer getOrderProgr() {
		return orderProgr;
	}

	public void setOrderProgr(Integer orderProgr) {
		this.orderProgr = orderProgr;
	}

	public String getDigLevel() {
		return digLevel;
	}

	public void setDigLevel(String digLevel) {
		this.digLevel = digLevel;
	}

	public int getCntlKeyNbr() {
		return cntlKeyNbr;
	}

	public void setCntlKeyNbr(int cntlKeyNbr) {
		this.cntlKeyNbr = cntlKeyNbr;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getProgressiveType() {
		return progressiveType;
	}

	public void setProgressiveType(String progressiveType) {
		this.progressiveType = progressiveType;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
}
