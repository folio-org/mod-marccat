package org.folio.cataloging.dao.persistence;

import java.util.Date;

public class UserTempPass {

  private String userName;
  private String pass;
  private String appCode;
  private Date lastAccess = new Date();


  public UserTempPass() {
    super();
    // TODO Auto-generated constructor stub
  }

  public UserTempPass(String pass, String userName, String appCode, Date lastAccess) {
    super();
    this.userName = userName;
    this.pass = pass;
    this.appCode = appCode;
    this.lastAccess = lastAccess;
  }

  public UserTempPass(String pass, String userName, String appCode) {
    super();
    this.userName = userName;
    this.pass = pass;
    this.appCode = appCode;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPass() {
    return pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  public String getAppCode() {
    return appCode;
  }

  public void setAppCode(String appCode) {
    this.appCode = appCode;
  }

  public Date getLastAccess() {
    return lastAccess;
  }

  public void setLastAccess(Date lastAccess) {
    this.lastAccess = lastAccess;
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pass == null) ? 0 : pass.hashCode());
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final UserTempPass other = (UserTempPass) obj;
    if (pass == null) {
      if (other.pass != null)
        return false;
    } else if (!pass.equals(other.pass))
      return false;
    return true;
  }

}
