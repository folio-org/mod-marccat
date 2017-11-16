package com.casalini.hibernate.model;

import java.io.Serializable;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.casalini.digital.bean.DigitalPoliciesBean;
import com.casalini.digital.business.DAOCasDigAdmin;
import com.libricore.librisuite.common.HibernateUtil;

public class CasDgaPolicy implements Persistence 
{
	private static final long serialVersionUID = 2522128570785338271L;

	static DAOCasDigAdmin dao = new DAOCasDigAdmin(); 
	
	private int     bibItemNumber;
	private String  idPolicy;
	private String  denPolicy;
	private String  typePolicy;
	private int     numStampaPolicy;
	private Float   pricePolicy;
	private String  curcyPolicy;
	private Float   priceTotal;
	
	public CasDgaPolicy(int bibItemNumber) {
		this();
		setBibItemNumber(bibItemNumber);
	}
	
	private PersistenceState persistenceState = new PersistenceState();
	
	public CasDgaPolicy() {
		super();
	}
	
	public CasDgaPolicy(DigitalPoliciesBean bean, int amicusNumber) 
	{
		this.bibItemNumber=amicusNumber;
		this.idPolicy=bean.getPolicyCode();
		this.denPolicy=bean.getPolicyName();
		this.typePolicy=bean.getPolicyType();
		this.numStampaPolicy=bean.getPolicyStamps();
		this.pricePolicy=bean.getPolicyPrice();
		this.curcyPolicy=bean.getPolicyCurcy();
		this.priceTotal=bean.getPolicyTotPrice();
	}

	public Float getPriceTotal() {
		return priceTotal;
	}

	public void setPriceTotal(Float priceTotal) {
		this.priceTotal = priceTotal;
	}

	public int getBibItemNumber() {
		return bibItemNumber;
	}

	public PersistenceState getPersistenceState() {
		return persistenceState;
	}

	public void setBibItemNumber(int i) {
		bibItemNumber = i;
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
		final CasDgaPolicy other = (CasDgaPolicy) obj;
		
		return (bibItemNumber==other.bibItemNumber && idPolicy.equals(other.idPolicy) && typePolicy.equals(other.typePolicy));
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
	}

	public String getIdPolicy() {
		return idPolicy;
	}

	public void setIdPolicy(String idPolicy) {
		this.idPolicy = idPolicy;
	}

	public String getDenPolicy() {
		return denPolicy;
	}

	public void setDenPolicy(String denPolicy) {
		this.denPolicy = denPolicy;
	}

	public String getTypePolicy() {
		return typePolicy;
	}

	public void setTypePolicy(String typePolicy) {
		this.typePolicy = typePolicy;
	}

	public int getNumStampaPolicy() {
		return numStampaPolicy;
	}

	public void setNumStampaPolicy(int numStampaPolicy) {
		this.numStampaPolicy = numStampaPolicy;
	}

	public Float getPricePolicy() {
		return pricePolicy;
	}

	public void setPricePolicy(Float pricePolicy) {
		this.pricePolicy = pricePolicy;
	}

	public String getCurcyPolicy() {
		return curcyPolicy;
	}

	public void setCurcyPolicy(String curcyPolicy) {
		this.curcyPolicy = curcyPolicy;
	}

}
