package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.shared.ValidationKey;

/**
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class BibliographicValidationKey extends ValidationKey {


  /**
   * Class constructor
   *
   * @since 1.0
   */
  public BibliographicValidationKey() {
    super ( );
  }

  /**
   * Builds a new {@link BibliographicValidationKey}.
   *
   * @param marcTag         the tag code.
   * @param marcTagCategory the tag category.
   */
  public BibliographicValidationKey(final String marcTag, final int marcTagCategory) {
    super (marcTag, marcTagCategory);
  }
}
