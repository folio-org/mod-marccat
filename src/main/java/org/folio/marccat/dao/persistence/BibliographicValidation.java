
package org.folio.marccat.dao.persistence;

import org.folio.marccat.shared.Validation;
import org.folio.marccat.shared.ValidationKey;

import java.io.Serializable;

/**
 * @author paulm
 * @since 1.0
 */
public class BibliographicValidation extends Validation implements Serializable {

  private BibliographicValidationKey key;

  public ValidationKey getKey() {
    return key;
  }

  public void setKey(BibliographicValidationKey validation) {
    key = validation;
  }

}
