package org.folio.marccat.dao.persistence;

import org.folio.marccat.shared.ValidationKey;

/**
 * @author elena
 * @since 1.0
 */
public class AuthorityValidationKey extends ValidationKey {

	private String headingType;

	public AuthorityValidationKey() {
		super();
	}

	/**
	 * Builds a new {@link AuthorityValidationKey}.
	 *
	 * @param marcTag         the tag code.
	 * @param marcTagCategory the tag category.
	 */
	public AuthorityValidationKey(final String marcTag, final int marcTagCategory) {
		super(marcTag, marcTagCategory);
	}

	/**
	 * Class constructor
	 *
	 * @param marcTag
	 * @param marcTagCategory
	 * @param headingType
	 * @since 1.0
	 */
	public AuthorityValidationKey(String marcTag, short marcTagCategory, String headingType) {
		super(marcTag, marcTagCategory);
		setHeadingType(headingType);
	}

	public String getHeadingType() {
		return headingType;
	}

	public void setHeadingType(String headingType) {
		this.headingType = headingType;
	}

	@Override
	public boolean equals(Object anObject) {
		if (anObject instanceof AuthorityValidationKey) {
			AuthorityValidationKey aKey = (AuthorityValidationKey) anObject;
			return (super.equals(aKey) && headingType == aKey.getHeadingType());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode() + headingType.hashCode();
	}

}
