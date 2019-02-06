/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * BibliographicAuthenticationCodeTag.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.AuthenticationCodeTag;


public class BibliographicAuthenticationCodeTag extends AuthenticationCodeTag {


  public BibliographicAuthenticationCodeTag() {
    super();
    setHeaderType((short) 4);
  }

}
