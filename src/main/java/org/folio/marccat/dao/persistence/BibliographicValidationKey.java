package org.folio.marccat.dao.persistence;

import org.folio.marccat.shared.ValidationKey;

/**
 * @author paulm
 * @author cchiama
 * @since 1.0
 */
public class BibliographicValidationKey extends ValidationKey {


  public BibliographicValidationKey() {
    super();
  }

  /**
   * Builds a new {@link BibliographicValidationKey}.
   *
   * @param marcTag         the tag code.
   * @param marcTagCategory the tag category.
   */
  public BibliographicValidationKey(final String marcTag, final int marcTagCategory) {
    super(marcTag, marcTagCategory);
  }
}
