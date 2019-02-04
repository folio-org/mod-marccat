package org.folio.marccat.business.cataloguing.common;

import org.folio.marccat.business.common.SortFormException;

public interface SortFormObject {
  /**
   * Calculates the value for the sortform and sets the appropriate
   * sortform member
   *
   * @throws SortFormException
   */
  void calculateAndSetSortForm() throws SortFormException;
}
