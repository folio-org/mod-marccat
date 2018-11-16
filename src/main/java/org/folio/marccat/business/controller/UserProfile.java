package org.folio.marccat.business.controller;

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
