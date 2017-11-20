/*
 * (c) LibriCore
 * 
 * Created on Jun 18, 2004
 * 
 * UserProfile.java
 */
package org.folio.cataloging.business.controller;

import org.folio.cataloging.dao.DAOUserAccount;
import org.folio.cataloging.business.authorisation.AmicusAuthority;
import org.folio.cataloging.business.authorisation.AuthorisationAgent;
import org.folio.cataloging.business.authorisation.AuthorisationException;
import org.folio.cataloging.business.authorisation.Permission;
import org.folio.cataloging.dao.DAOOrganisationHierarchy;
import org.folio.cataloging.business.common.DataAccessException;
import org.folio.cataloging.business.common.Defaults;
import org.folio.cataloging.dao.persistence.ORG_HRCHY;
import org.folio.cataloging.dao.persistence.T_ITM_DSPLY;
import org.folio.cataloging.dao.persistence.USR_ACNT;

import org.folio.cataloging.dao.common.HibernateUtil;

/**
 * User information affecting application behaviour
 * @author paulm
 */
public class UserProfile {
	private int cataloguingView;
	// pm 2011
	private int mainLibrary;
	private AuthorisationAgent authorisationAgent;
	private String name;
	private int maxRecordCount;
	String startApp = Defaults.getString("starting.application");
	private USR_ACNT aUserAccount;
    
	public UserProfile(String name) throws DataAccessException {
		aUserAccount = new DAOUserAccount().load(name);
		if(aUserAccount==null) {
			throw new NullPointerException("User "+name+" not found in the database");
		}
		ORG_HRCHY anOrgHierarchy = new DAOOrganisationHierarchy().load(aUserAccount.getBranchLibrary());
		int realView = getRealUserView(aUserAccount);
		setCataloguingView(realView);
		
		// pm 2011
		setSearchingView(aUserAccount.getDefaultSearchingView());
		setDatabasePreferenceOrder(aUserAccount.getDatabasePreferenceOrder());
		setDefaultRecordDisplay(aUserAccount.getDefaultRecordDisplay());
		setBranchLibrary(aUserAccount.getBranchLibrary());
		setMainLibrary(anOrgHierarchy.getPARNT_ORG_NBR());
		this.name = aUserAccount.getName();
		this.maxRecordCount = aUserAccount.getMaxRecordCount();
		setAuthorisationAgent(new AmicusAuthority(name));
		setDefaultAuthorityModel(aUserAccount.getDefaultAuthorityModel());
		setDefaultBibliographicModel(aUserAccount.getDefaultBibliographicModel());
	}

	//TODO PM This default constructor was added when the functionality of the (name) constructor was duplicated
	// in UserProfileDAO -- and the underlying persistence was changed from Hibernate to JDBC -- Not sure why this 
	// was done (check with Emiliano) and then eliminate the duplication and decide on the persistence model.
	// -- in the meantime -- initialize a "new" USR_ACNT object within the default constructor
//	Cammilletti inizio
	public UserProfile()
	{
		aUserAccount = new USR_ACNT();
	}
//	Cammilletti fine
	
	/**
	 * It depends upon the starting application "mad"
	 * TODO _MIKE: Both mades and LibriCat cataloguing view can be active simultaneously
	 * @param aUserAccount
	 * @return
	 */
	private int getRealUserView(USR_ACNT aUserAccount) {
		int uaView = aUserAccount.getCataloguingView();
		int realView = uaView;
		if("mad".equalsIgnoreCase(startApp)){
			realView = convert(uaView);
		}
		return realView;
	}

	/**
	 * It depends upon the starting application "mad"
	 * TODO _MIKE: Both mades and LibriCat cataloguing view can be active simultaneously
	 * @return
	 */
	public int getRealUserView(int uaView ) {
		int realView = uaView;
		return realView;
	}
	
	/**
	 * Converte la vista catalografica dell'utente nella vista interna Mades
	 * secondo la seguente tabella:
	 * <pre>
	 * userView  | madesView
	 * ----------+-----------
	 *    1      |    -2
	 *    2      |    -3
	 *    3      |    -4
	 * ----------^-----------
	 * </pre>
	 * @param userView
	 * @return
	 */
	private int convert(int userView) {
		
		return -(userView + 1);
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
	 * Setter for cataloguingView
	 * 
	 * @param s cataloguingView
	 */
	public void setCataloguingView(int s) {
		cataloguingView = s;
	}

	public int getBranchLibrary() {
		return aUserAccount.getBranchLibrary();
	}

	public void setBranchLibrary(int i) {
		aUserAccount.setBranchLibrary(i);
	}

	public int getMainLibrary() {
		return mainLibrary;
	}

	public void setMainLibrary(int i) {
		mainLibrary = i;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public AuthorisationAgent getAuthorisationAgent() {
		return authorisationAgent;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setAuthorisationAgent(AuthorisationAgent agent) {
		authorisationAgent = agent;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void checkPermission(Permission aPermission)
		throws AuthorisationException {
		authorisationAgent.checkPermission(aPermission);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void checkPermission(Permission[] somePermissions)
		throws AuthorisationException {
		authorisationAgent.checkPermission(somePermissions);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void checkPermission(String permissionName)
		throws AuthorisationException {
		authorisationAgent.checkPermission(permissionName);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void checkPermission(String[] someNames)
		throws AuthorisationException {
		authorisationAgent.checkPermission(someNames);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public T_ITM_DSPLY getDefaultRecordDisplay() {
		return aUserAccount.getDefaultRecordDisplay();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDefaultRecordDisplay(T_ITM_DSPLY t_itm_dsply) {
		aUserAccount.setDefaultRecordDisplay(t_itm_dsply);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getMaxRecordCount() {
		return maxRecordCount;
	}

	public String getCataloguingViewString(){
		if(cataloguingView<0) {
			String s = Defaults.getString("catalog.view.label."+cataloguingView);
			if(s!=null) return s;
		}
		return "" + cataloguingView;
	}
	
	/**
	 * pm 2011
	 * @return
	 */
	public int getSearchingView() {
		return aUserAccount.getDefaultSearchingView();
	}
	/**
	 * pm 2011
	 * @param searchingView
	 */
	public void setSearchingView(int searchingView) {
		aUserAccount.setDefaultSearchingView(searchingView);
	}
	
	/**
	 * pm 2011
	 * @return the databasePreferenceOrder
	 */
	public int getDatabasePreferenceOrder() {
		return aUserAccount.getDatabasePreferenceOrder();
	}

	/**
	 * pm 2011
	 * @param databasePreferenceOrder the databasePreferenceOrder to set
	 */
	public void setDatabasePreferenceOrder(int databasePreferenceOrder) {
		aUserAccount.setDatabasePreferenceOrder(databasePreferenceOrder);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaxRecordCount(int maxRecordCount) {
		this.maxRecordCount = maxRecordCount;
	}

	public Integer getDefaultBibliographicModel() {
		return aUserAccount.getDefaultBibliographicModel();
	}

	public void setDefaultBibliographicModel(Integer defaultBibliographicModel) {
		aUserAccount.setDefaultBibliographicModel(defaultBibliographicModel);
	}

	public Integer getDefaultAuthorityModel() {
		return aUserAccount.getDefaultAuthorityModel();
	}

	public void setDefaultAuthorityModel(Integer defaultAuthorityModel) {
		aUserAccount.setDefaultAuthorityModel(defaultAuthorityModel);
	}
	public void persistAccountSettings() throws DataAccessException {
		new HibernateUtil().update(aUserAccount);
	}
}
