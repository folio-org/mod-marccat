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
  private String name;
  private USR_ACNT aUserAccount;

  public UserProfile() {
    aUserAccount = new USR_ACNT();
  }


  public int getCataloguingView() {
    return cataloguingView;
  }

  public void setCataloguingView(int s) {
    cataloguingView = s;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSearchingView() {
    return aUserAccount.getDefaultSearchingView();
  }

  public void setSearchingView(int searchingView) {
    aUserAccount.setDefaultSearchingView(searchingView);
  }

}
