package librisuite.business.cataloguing.bibliographic;

import librisuite.business.cataloguing.common.DateOfLastTransactionTag;
import librisuite.business.common.PersistenceState;

public class BibliographicDateOfLastTransactionTag extends DateOfLastTransactionTag {
	public BibliographicDateOfLastTransactionTag() {
		super();
		setHeaderType((short)41);
		setPersistenceState(new PersistenceState());
	}
}