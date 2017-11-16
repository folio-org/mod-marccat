/*
 * (c) LibriCore
 * 
 * Created on Jan 6, 2006
 * 
 * SeeAlsoReferenceTag.java
 */
package librisuite.business.cataloguing.authority;

import librisuite.business.common.CorrelationValues;
import librisuite.hibernate.REF;
import librisuite.hibernate.ReferenceType;

/**
 * @author paulm
 * @version $Revision: 1.1 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class SeeAlsoReferenceTag extends SeeSeeAlsoReference {
	private REF dualReference = null;

	private short dualReferenceIndicator = 0;

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public SeeAlsoReferenceTag() {
		super();
	}

	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		if (!super.correlationChangeAffectsKey(v)) {
			return !ReferenceType.isSeeAlsoFrom(
				v.getValue(getRefTypeCorrelationPosition()));
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @since 1.0
	 */
	public REF getDualReference() {
		return dualReference;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public short getDualReferenceIndicator() {
		return dualReferenceIndicator;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.authority.SeeSeeAlsoReference#getHasDualIndicator()
	 */
	public boolean isHasDualIndicator() {
		return true;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDualReference(REF ref) {
		dualReference = ref;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDualReferenceIndicator(short s) {
		dualReferenceIndicator = s;
	}

}
