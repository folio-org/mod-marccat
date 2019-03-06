/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * AuthorityValidationKey.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.shared.ValidationKey;

import java.io.Serializable;

/**
 * @author paulm
 * @version $Revision: 1.2 $, $Date: 2005/12/01 13:50:05 $
 * @since 1.0
 */
public class AuthorityValidationKey extends ValidationKey implements Serializable {

  private String headingType;


  public AuthorityValidationKey() {
    super();

  }



  public String getHeadingType() {
    return headingType;
  }


  public void setHeadingType(String string) {
    headingType = string;
  }

}
