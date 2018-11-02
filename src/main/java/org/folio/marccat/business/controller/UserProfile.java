package org.folio.marccat.business.controller;

import net.sf.hibernate.Session;
import org.folio.marccat.business.authorisation.AmicusAuthority;
import org.folio.marccat.business.authorisation.AuthorisationAgent;
import org.folio.marccat.business.authorisation.AuthorisationException;
import org.folio.marccat.business.authorisation.Permission;
import org.folio.marccat.business.common.DataAccessException;
import org.folio.marccat.dao.DAOOrganisationHierarchy;
import org.folio.marccat.dao.DAOUserAccount;
import org.folio.marccat.dao.common.HibernateUtil;
import org.folio.marccat.dao.persistence.ORG_HRCHY;
import org.folio.marccat.dao.persistence.T_ITM_DSPLY;
import org.folio.marccat.dao.persistence.USR_ACNT;

/**
 * User information affecting application behaviour.
 *
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public class UserProfile {
  private int cataloguingView;

  private int mainLibrary;
  private AuthorisationAgent authorisationAgent;
  private String name;
  private int maxRecordCount;
  private USR_ACNT aUserAccount;

  public UserProfile(final Session session, String name) throws DataAccessException {
    aUserAccount = new DAOUserAccount().load(name);
    if (aUserAccount == null) {
      throw new NullPointerException("User " + name + " not found in the database");
    }
    ORG_HRCHY anOrgHierarchy = new DAOOrganisationHierarchy().load(session, aUserAccount.getBranchLibrary());
    int realView = getRealUserView(aUserAccount);
    setCataloguingView(realView);


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

  public UserProfile() {
    aUserAccount = new USR_ACNT();
  }

  private int getRealUserView(final USR_ACNT userAccount) {
    return userAccount.getCataloguingView();
  }

  public int getCataloguingView() {
    return cataloguingView;
  }

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

  public AuthorisationAgent getAuthorisationAgent() {
    return authorisationAgent;
  }

  public void setAuthorisationAgent(AuthorisationAgent agent) {
    authorisationAgent = agent;
  }

  public void checkPermission(final Permission aPermission) throws AuthorisationException {
    authorisationAgent.checkPermission(aPermission);
  }

  public void checkPermission(final Permission[] somePermissions) throws AuthorisationException {
    authorisationAgent.checkPermission(somePermissions);
  }

  public void checkPermission(final String permissionName) throws AuthorisationException {
    authorisationAgent.checkPermission(permissionName);
  }

  public void checkPermission(final String[] someNames) throws AuthorisationException {
    authorisationAgent.checkPermission(someNames);
  }

  public T_ITM_DSPLY getDefaultRecordDisplay() {
    return aUserAccount.getDefaultRecordDisplay();
  }

  public void setDefaultRecordDisplay(T_ITM_DSPLY t_itm_dsply) {
    aUserAccount.setDefaultRecordDisplay(t_itm_dsply);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getMaxRecordCount() {
    return maxRecordCount;
  }

  public void setMaxRecordCount(int maxRecordCount) {
    this.maxRecordCount = maxRecordCount;
  }

  public int getSearchingView() {
    return aUserAccount.getDefaultSearchingView();
  }

  public void setSearchingView(int searchingView) {
    aUserAccount.setDefaultSearchingView(searchingView);
  }

  public int getDatabasePreferenceOrder() {
    return aUserAccount.getDatabasePreferenceOrder();
  }

  public void setDatabasePreferenceOrder(int databasePreferenceOrder) {
    aUserAccount.setDatabasePreferenceOrder(databasePreferenceOrder);
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
