package librisuite.hibernate;

import java.io.Serializable;

import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.libricore.librisuite.common.HibernateUtil;

public class CLCTN_MST_RULE_REL implements Persistence, Comparable
{
	private static final long serialVersionUID = 2522128570785338271L;

//	static DAOCollectionRule dao = new DAOCollectionRule();
	
	private Integer ruleId;
	private Long idCollection;
	
	private PersistenceState persistenceState = new PersistenceState();

	public CLCTN_MST_RULE_REL() {
		super();
	}
	
	public CLCTN_MST_RULE_REL(Integer id, Long coll) {
		this.idCollection=coll;
		this.ruleId=id;
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
		return null;
	}
	
	public int getUpdateStatus() {
		return persistenceState.getUpdateStatus();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((idCollection == null) ? 0 : idCollection.hashCode());
		result = prime * result + ((ruleId == null) ? 0 : ruleId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CLCTN_MST_RULE_REL other = (CLCTN_MST_RULE_REL) obj;
		if (idCollection == null) {
			if (other.idCollection != null)
				return false;
		} else if (!idCollection.equals(other.idCollection))
			return false;
		if (ruleId == null) {
			if (other.ruleId != null)
				return false;
		} else if (!ruleId.equals(other.ruleId))
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

	public void generateNewKey() throws DataAccessException {
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Long getIdCollection() {
		return idCollection;
	}

	public void setIdCollection(Long idCollection) {
		this.idCollection = idCollection;
	}

	public int compareTo(Object o) 
	{
		CLCTN_MST_RULE_REL collRule = (CLCTN_MST_RULE_REL)o;
//		int result = new Long(this.idCollection).compareTo(new Long(collRule.getIdCollection()));
		int result = this.idCollection.compareTo(collRule.getIdCollection());
		return result;
	}
}