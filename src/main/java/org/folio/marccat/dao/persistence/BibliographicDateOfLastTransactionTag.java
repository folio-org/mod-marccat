/*
 * (c) LibriCore
 *
 * Created on Nov 18, 2005
 *
 * BibliographicDateOfLastTransactionTag.java
 */
package org.folio.marccat.dao.persistence;

import org.folio.marccat.business.cataloguing.common.DateOfLastTransactionTag;
import org.folio.marccat.business.common.PersistenceState;


public class BibliographicDateOfLastTransactionTag
  extends DateOfLastTransactionTag {


  public BibliographicDateOfLastTransactionTag() {
    super();
    setHeaderType((short) 41);
    setPersistenceState(new PersistenceState());
  }

}
