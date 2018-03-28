package org.folio.cataloging.dao.persistence;

import org.folio.cataloging.shared.ValidationKey;

/**
 * @author paulm
 * @since 1.0
 */
public class BibliographicValidationKey extends ValidationKey {
	/**
	 * Class constructor
	 *
	 * @param marcTag
	 * @param marcTagCategory
	 * @since 1.0
	 */
	public BibliographicValidationKey(final String marcTag, final int marcTagCategory) {
		super(marcTag, marcTagCategory);
	}

}
