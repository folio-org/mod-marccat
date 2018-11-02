/*
 * (c) LibriCore
 *
 * Created on Nov 19, 2004
 *
 * TagAuthorisation.java
 */
package org.folio.marccat.business.authorisation;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2004/11/19 16:48:32 $
 * @since 1.0
 */
public class TagAuthorisation implements Serializable {
  private String userAccount;
  private short bibliographicOrAuthorityCode;
  private short tagCategory;

  /**
   * @since 1.0
   */
  public short getBibliographicOrAuthorityCode() {
    return bibliographicOrAuthorityCode;
  }

  /**
   * @since 1.0
   */
  public void setBibliographicOrAuthorityCode(short s) {
    bibliographicOrAuthorityCode = s;
  }

  /**
   * @since 1.0
   */
  public short getTagCategory() {
    return tagCategory;
  }

  /**
   * @since 1.0
   */
  public void setTagCategory(short s) {
    tagCategory = s;
  }

  /**
   * @since 1.0
   */
  public String getUserAccount() {
    return userAccount;
  }

  /**
   * @since 1.0
   */
  public void setUserAccount(String string) {
    userAccount = string;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object arg0) {
    if (arg0 instanceof TagAuthorisation) {
      TagAuthorisation ta = (TagAuthorisation) arg0;
      return this.getUserAccount().equals(ta.getUserAccount())
        && this.getBibliographicOrAuthorityCode()
        == ta.getBibliographicOrAuthorityCode()
        && this.getTagCategory() == ta.getTagCategory();
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return getUserAccount().hashCode() + getTagCategory();
  }

}
