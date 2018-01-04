package org.folio.cataloging.bean.cataloguing.bibliographic.codelist;

import org.folio.cataloging.dao.persistence.T_VRFTN_LVL;

/**
 * Record verification level.
 *
 * @author natascia
 * @since 1.0
 */
public class VerificationLevel extends CodeListBean {
	/**
	 * Builds a a new {@link VerificationLevel} with the given data.
	 */
	public VerificationLevel() {
		super(T_VRFTN_LVL.class);
	}
}
