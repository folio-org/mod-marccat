package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import librisuite.business.cataloguing.copy.DAOHeadingUri;
import librisuite.business.common.DataAccessException;
import librisuite.business.common.Persistence;
import librisuite.business.common.PersistenceState;
import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

import com.casalini.approval.IGlobalConst;
import com.libricore.librisuite.common.HibernateUtil;

public class HDG_URI implements Persistence
{	
	private static final long serialVersionUID = 6496002006152292469L;
	
	static DAOHeadingUri dao = new DAOHeadingUri();
	
	private String uri;
	private int headingNumber;   
	private String userView;
    private int headingTypeCode; 
	private int sourceId;
	private String validatedCheck; 
	private String lastUpdateUser;
	private Date lastUpdateDate; 
    private String creationUser; 
    private Date creationDate;
    
	private String sourceDenomination;
	private String uriLabel; 
	
	private boolean disabled;
	
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getHeadingNumber() {
		return headingNumber;
	}
	public void setHeadingNumber(int headingNumber) {
		this.headingNumber = headingNumber;
	}
	public String getUserView() {
		return userView;
	}
	public void setUserView(String userView) {
		this.userView = userView;
	}
	public int getHeadingTypeCode() {
		return headingTypeCode;
	}
	public void setHeadingTypeCode(int headingTypeCode) {
		this.headingTypeCode = headingTypeCode;
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public String getValidatedCheck() {
		return validatedCheck;
	}
	public void setValidatedCheck(String validatedCheck) {
		this.validatedCheck = validatedCheck;
	}
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getCreationUser() {
		return creationUser;
	}
	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getSourceDenomination() {
		return sourceDenomination;
	}
	public void setSourceDenomination(String sourceDenomination) {
		this.sourceDenomination = sourceDenomination;
	}
	public String getUriLabel() {
		return uriLabel;
	}
	public void setUriLabel(String uriLabel) {
		this.uriLabel = uriLabel;
	}
		
	public String getCreationDateString() 
	{
		return (creationDate != null?IGlobalConst.formatter.format(creationDate):"");
	}
	
	public String getLastUpdateDateString() 
	{
		return (lastUpdateDate != null?IGlobalConst.formatter.format(lastUpdateDate):"");
	}

	private PersistenceState persistenceState = new PersistenceState();

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + headingNumber;
		result = prime * result + headingTypeCode;
		result = prime * result + sourceId;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result
				+ ((userView == null) ? 0 : userView.hashCode());
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
		final HDG_URI other = (HDG_URI) obj;
		if (headingNumber != other.headingNumber)
			return false;
		if (headingTypeCode != other.headingTypeCode)
			return false;
		if (sourceId != other.sourceId)
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (userView == null) {
			if (other.userView != null)
				return false;
		} else if (!userView.equals(other.userView))
			return false;
		return true;
	}
		
	public String toString() 
	{
	   return ToStringBuilder.reflectionToString(this);
	}
	
	public boolean isEnableToAlternativeLabelsSearch() 
	{			
		return (IGlobalConst.sourcesEnabledToAlternativeLabelsSearch.contains(new Integer(sourceId))?true:false);
	}
}