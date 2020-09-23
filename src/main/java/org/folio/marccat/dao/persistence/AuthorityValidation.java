
package org.folio.marccat.dao.persistence;

import java.io.Serializable;

import org.folio.marccat.shared.Validation;
import org.folio.marccat.shared.ValidationKey;

/**
 * @author elena
 * @since 1.0
 */
public class AuthorityValidation extends Validation implements Serializable {

  private AuthorityValidationKey key;

  public ValidationKey getKey() {
    return key;
  }

  public void setKey(AuthorityValidationKey validation) {
    key = validation;
  }

}
