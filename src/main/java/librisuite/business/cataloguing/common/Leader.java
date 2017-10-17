package librisuite.business.cataloguing.common;

import librisuite.business.cataloguing.bibliographic.FixedFieldUsingItemEntity;

public abstract class Leader extends FixedFieldUsingItemEntity {

	public boolean isAbleToBeDeleted() {
		return false;
	}
}
