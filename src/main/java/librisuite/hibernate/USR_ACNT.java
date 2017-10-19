package librisuite.hibernate;

import java.io.Serializable;
import java.util.Date;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.Session;

public class USR_ACNT implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private int orderNumber;
	private int orderDetailNumber;
	private int databasePreferenceOrder;
	private int cataloguingView;
	private int personNumber;
	private int branchLibrary;
	private int systemKeyNumber;
	private String profileTemplateName;
	private String operatingSystemGroupName;
	private short lockingVersionUpdateNumber;
	private int defaultSearchingView;
	private int defaultBibTemplateNumber;
	private int maxRecordCount;
	private short communicationsAccessType;
	private short defaultAddressType;
	private T_ITM_DSPLY defaultRecordDisplay;
	private short defaultMarcDisplay;
	private short defaultFullDisplay;
	private short defaultBriefDisplay;
	private boolean showResultsIndicator;
	private boolean accountActiveIndicator;
	private Date lastLogonDate;
	private Date passwordExpiryDate;
	private String note;
	private String queryFilter;
	private int displayLimit;
	private Integer defaultAuthorityModel;
	private Integer defaultBibliographicModel;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final USR_ACNT other = (USR_ACNT) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * Getter for branchLibrary
	 * 
	 * @return branchLibrary
	 */
	public int getBranchLibrary() {
		return branchLibrary;
	}

	/**
	 * Getter for cataloguingView
	 * 
	 * @return cataloguingView
	 */
	public int getCataloguingView() {
		return cataloguingView;
	}

	/**
	 * Getter for communicationsAccessType
	 * 
	 * @return communicationsAccessType
	 */
	public short getCommunicationsAccessType() {
		return communicationsAccessType;
	}


	/**
	 * Getter for databasePreferenceOrder
	 * 
	 * @return databasePreferenceOrder
	 */
	public int getDatabasePreferenceOrder() {
		return databasePreferenceOrder;
	}

	/**
	 * Getter for defaultAddresType
	 * 
	 * @return defaultAddresType
	 */
	public short getDefaultAddressType() {
		return defaultAddressType;
	}

	public Integer getDefaultAuthorityModel() {
		return defaultAuthorityModel;
	}

	public Integer getDefaultBibliographicModel() {
		return defaultBibliographicModel;
	}

	/**
	 * Getter for defaultBibTemplateNumber
	 * 
	 * @return defaultBibTemplateNumber
	 */
	public int getDefaultBibTemplateNumber() {
		return defaultBibTemplateNumber;
	}

	/**
	 * Getter for defaultBriefDisplay
	 * 
	 * @return defaultBriefDisplay
	 */
	public short getDefaultBriefDisplay() {
		return defaultBriefDisplay;
	}

	/**
	 * Getter for defaultFullDisplay
	 * 
	 * @return defaultFullDisplay
	 */
	public short getDefaultFullDisplay() {
		return defaultFullDisplay;
	}

	/**
	 * Getter for defaultMarcDisplay
	 * 
	 * @return defaultMarcDisplay
	 */
	public short getDefaultMarcDisplay() {
		return defaultMarcDisplay;
	}

	/**
	 * Getter for defaultRecordDisplay
	 * 
	 * @return defaultRecordDisplay
	 */
	public T_ITM_DSPLY getDefaultRecordDisplay() {
		return defaultRecordDisplay;
	}

	/**
	 * Getter for defaultSearchingView
	 * 
	 * @return defaultSearchingView
	 */
	public int getDefaultSearchingView() {
		return defaultSearchingView;
	}

	/**
	 * Getter for displayLimit
	 * 
	 * @return displayLimit
	 */
	public int getDisplayLimit() {
		return displayLimit;
	}

	/**
	 * Getter for lastLogonDate
	 * 
	 * @return lastLogonDate
	 */
	public Date getLastLogonDate() {
		return lastLogonDate;
	}

	/**
	 * Getter for lockingVersionUpdateNumber
	 * 
	 * @return lockingVersionUpdateNumber
	 */
	public short getLockingVersionUpdateNumber() {
		return lockingVersionUpdateNumber;
	}

	/**
	 * Getter for maxRecordCound
	 * 
	 * @return maxRecordCound
	 */
	public int getMaxRecordCount() {
		return maxRecordCount;
	}

	/**
	 * Getter for name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for note
	 * 
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Getter for operatingSystemGroupName
	 * 
	 * @return operatingSystemGroupName
	 */
	public String getOperatingSystemGroupName() {
		return operatingSystemGroupName;
	}

	/**
	 * Getter for orderDetailNumber
	 * 
	 * @return orderDetailNumber
	 */
	public int getOrderDetailNumber() {
		return orderDetailNumber;
	}

	/**
	 * Getter for orderNumber
	 * 
	 * @return orderNumber
	 */
	public int getOrderNumber() {
		return orderNumber;
	}

	/**
	 * Getter for passwordExpiryDate
	 * 
	 * @return passwordExpiryDate
	 */
	public Date getPasswordExpiryDate() {
		return passwordExpiryDate;
	}

	/**
	 * Getter for passwordNumber
	 * 
	 * @return passwordNumber
	 */
	public int getPersonNumber() {
		return personNumber;
	}

	/**
	 * Getter for profileTemplateName
	 * 
	 * @return profileTemplateName
	 */
	public String getProfileTemplateName() {
		return profileTemplateName;
	}

	/**
	 * Getter for queryFilter
	 * 
	 * @return queryFilter
	 */
	public String getQueryFilter() {
		return queryFilter;
	}

	/**
	 * Getter for systemKeyNumber
	 * 
	 * @return systemKeyNumber
	 */
	public int getSystemKeyNumber() {
		return systemKeyNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Getter for accountActiveIndicator
	 * 
	 * @return accountActiveIndicator
	 */

	/**
	 * Getter for showResultsIndicator
	 * 
	 * @return showResultsIndicator
	 */
	public boolean isShowResultsIndicator() {
		return showResultsIndicator;
	}
	/**
	 * Setter for accountActiveIndicator
	 * 
	 * @param b accountActiveIndicator
	 */
	public void setAccountActiveIndicator(boolean b) {
		accountActiveIndicator = b;
	}

	public boolean isAccountActiveIndicator() {
		return accountActiveIndicator;
	}

	/**
	 * Setter for branchLibrary
	 * 
	 * @param i branchLibrary
	 */
	public void setBranchLibrary(int i) {
		branchLibrary = i;
	}

	/**
	 * Setter for cataloguingView
	 * 
	 * @param i cataloguingView
	 */
	public void setCataloguingView(int i) {
		cataloguingView = i;
	}

	/**
	 * Setter for communicationAccessType
	 * 
	 * @param s communicationAccessType
	 */
	public void setCommunicationsAccessType(short s) {
		communicationsAccessType = s;
	}

	/**
	 * Setter for databasePreferenceOrder
	 * 
	 * @param i databasePreferenceOrder
	 */
	public void setDatabasePreferenceOrder(int i) {
		databasePreferenceOrder = i;
	}

	/**
	 * Setter for defaultAddressType
	 * 
	 * @param s defaultAddressType
	 */
	public void setDefaultAddressType(short s) {
		defaultAddressType = s;
	}

	public void setDefaultAuthorityModel(Integer defaultAuthorityModel) {
		this.defaultAuthorityModel = defaultAuthorityModel;
	}

	public void setDefaultBibliographicModel(Integer defaultBibliographicModel) {
		this.defaultBibliographicModel = defaultBibliographicModel;
	}

	/**
	 * Setter for defaultBibTemplateNumber
	 * 
	 * @param i defaultBibTemplateNumber
	 */
	public void setDefaultBibTemplateNumber(int i) {
		defaultBibTemplateNumber = i;
	}

	/**
	 * Setter for defaultBriefDisplay
	 * 
	 * @param s defaultBriefDisplay
	 */
	public void setDefaultBriefDisplay(short s) {
		defaultBriefDisplay = s;
	}

	/**
	 * Setter for defaultFullDisplay
	 * 
	 * @param s defaultFullDisplay
	 */
	public void setDefaultFullDisplay(short s) {
		defaultFullDisplay = s;
	}

	/**
	 * Setter for defaultMarcDisplay
	 * 
	 * @param s defaultMarcDisplay
	 */
	public void setDefaultMarcDisplay(short s) {
		defaultMarcDisplay = s;
	}

	/**
	 * Setter for defaultRecordDisplay
	 * 
	 * @param s defaultRecordDisplay
	 */
	public void setDefaultRecordDisplay(T_ITM_DSPLY s) {
		defaultRecordDisplay = s;
	}

	/**
	 * Setter for defaultSearchingView
	 * 
	 * @param i defaultSearchingView
	 */
	public void setDefaultSearchingView(int i) {
		defaultSearchingView = i;
	}

	/**
	 * Setter for displayLimit
	 * 
	 * @param i displayLimit
	 */
	public void setDisplayLimit(int i) {
		displayLimit = i;
	}

	/**
	 * Setter for lastLogonDate
	 * 
	 * @param date lastLogonDate
	 */
	public void setLastLogonDate(Date date) {
		lastLogonDate = date;
	}

	/**
	 * Setter for lockingVersionUpdateNumber
	 * 
	 * @param s lockingVersionUpdateNumber
	 */
	public void setLockingVersionUpdateNumber(short s) {
		lockingVersionUpdateNumber = s;
	}

	/**
	 * Setter for maxRecordCount
	 * 
	 * @param i maxRecordCount
	 */
	public void setMaxRecordCount(int i) {
		maxRecordCount = i;
	}

	/**
	 * Setter for name
	 * 
	 * @param string name
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * Setter for note
	 * 
	 * @param string note
	 */
	public void setNote(String string) {
		note = string;
	}

	/**
	 * Setter for operatingSystemGroupName
	 * 
	 * @param string operatingSystemGroupName
	 */
	public void setOperatingSystemGroupName(String string) {
		operatingSystemGroupName = string;
	}

	/**
	 * Setter for orderDetailNumber
	 * 
	 * @param i orderDetailNumber
	 */
	public void setOrderDetailNumber(int i) {
		orderDetailNumber = i;
	}

	/**
	 * Setter for orderNumber
	 * 
	 * @param i orderNumber
	 */
	public void setOrderNumber(int i) {
		orderNumber = i;
	}

	/**
	 * Setter for passwordExpiryDate
	 * 
	 * @param date passwordExpiryDate
	 */
	public void setPasswordExpiryDate(Date date) {
		passwordExpiryDate = date;
	}

	/**
	 * Setter for personNumber
	 * 
	 * @param i personNumber
	 */
	public void setPersonNumber(int i) {
		personNumber = i;
	}

	/**
	 * Setter for profileTemplateName
	 * 
	 * @param string profileTemplateName
	 */
	public void setProfileTemplateName(String string) {
		profileTemplateName = string;
	}

	/**
	 * Setter for queryFilter
	 * 
	 * @param string queryFilter
	 */
	public void setQueryFilter(String string) {
		queryFilter = string;
	}

	/**
	 * Setter for showResultsIndicator
	 * 
	 * @param b showResultsIndicator
	 */
	public void setShowResultsIndicator(boolean b) {
		showResultsIndicator = b;
	}

	/**
	 * Setter for systemKeyNumber
	 * 
	 * @param i systemKeyNumber
	 */
	public void setSystemKeyNumber(int i) {
		systemKeyNumber = i;
	}


}
