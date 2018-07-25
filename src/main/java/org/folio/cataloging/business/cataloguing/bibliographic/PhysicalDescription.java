package org.folio.cataloging.business.cataloguing.bibliographic;

import org.folio.cataloging.business.common.PersistenceState;
import org.folio.cataloging.business.common.PersistentObjectWithView;
import org.folio.cataloging.business.common.UserViewHelper;
import org.folio.cataloging.dao.AbstractDAO;
import org.folio.cataloging.shared.CorrelationValues;

/**
 * @author paulm
 * @since 1.0
 */
public abstract class PhysicalDescription extends FixedField implements PersistentObjectWithView {

	private char generalMaterialDesignationCode;
	private int keyNumber;
	private UserViewHelper userViewHelper = new UserViewHelper();

	public PhysicalDescription() {
		setPersistenceState(new PersistenceState());
	}

	public boolean isMap() {
		return false;
	}

	public boolean isElectronicResource() {
		return false;
	}

	public boolean isGlobe() {
		return false;
	}

	public boolean isTactileMaterial() {
		return false;
	}

	public boolean isProjectedGraphic() {
		return false;
	}

	public boolean isMicroform() {
		return false;
	}

	public boolean isNonProjectedGraphic() {
		return false;
	}

	public boolean isMotionPicture() {
		return false;
	}

	public boolean isKit() {
		return false;
	}

	public boolean isNotatedMusic() {
		return false;
	}

	public boolean isRemoteSensingImage() {
		return false;
	}

	public boolean isSoundRecording() {
		return false;
	}

	public boolean isText() {
		return false;
	}

	public boolean isVideoRecording() {
		return false;
	}

	public boolean isUnspecified() {
		return false;
	}

    @Override
	public String getDisplayString() {
		return null;
	}

	public char getGeneralMaterialDesignationCode() {
		return generalMaterialDesignationCode;
	}

	public void setGeneralMaterialDesignationCode(char c) {
		generalMaterialDesignationCode = c;
	}

	public int getKeyNumber() {
		return keyNumber;
	}

	public void setKeyNumber(int i) {
		keyNumber = i;
	}

    @Override
	public boolean equals(Object obj) {
		if (obj instanceof PhysicalDescription) {
			if (super.equals(obj)) {
				return ((PhysicalDescription) obj).getKeyNumber()
					== this.getKeyNumber()
					&& ((PhysicalDescription) obj).getHeaderType()
						== this.getHeaderType();
			}
		}
		return false;
	}

    @Override
	public int hashCode() {
		return getBibItemNumber() + getKeyNumber();
	}

    @Override
	public boolean isAbleToBeDeleted() {
		return true;
	}

	protected char specificMaterialDesignationCode = 'd';

	public char getSpecificMaterialDesignationCode() {
		return specificMaterialDesignationCode;
	}

	public void setSpecificMaterialDesignationCode(char c) {
		specificMaterialDesignationCode = c;
	}

    @Override
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1);
	}

    @Override
	public AbstractDAO getDAO() {
		return getPersistenceState().getDAO();
	}

	@Override
	public String getUserViewString() {
		return userViewHelper.getUserViewString();
	}

	@Override
	public void setUserViewString(String string) {
		userViewHelper.setUserViewString(string);
	}

	public int getBibItemNumber() {
		return getItemNumber();
	}

	public void setBibItemNumber(int i) {
		setItemNumber(i);
	}
}
