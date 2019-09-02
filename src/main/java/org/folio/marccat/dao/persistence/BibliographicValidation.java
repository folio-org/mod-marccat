package org.folio.marccat.dao.persistence;

import org.folio.marccat.shared.Validation;
import org.folio.marccat.shared.ValidationKey;

import java.io.Serializable;

/**
 * Class comment S_BIB_MARC_TAG_VLDTN
 *
 * @author paulm
 */
public class BibliographicValidation extends Validation implements Serializable {

  private BibliographicValidationKey key;

  public ValidationKey getKey() {
    return key;
  }

  private void setKey(BibliographicValidationKey validation) {
    key = validation;
  }

}
