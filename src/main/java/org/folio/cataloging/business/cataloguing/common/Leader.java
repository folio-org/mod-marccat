package org.folio.cataloging.business.cataloguing.common;

import org.folio.cataloging.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;

public abstract class Leader extends FixedFieldUsingItemEntity {

	public boolean isAbleToBeDeleted() {
		return false;
	}
}
