/*
 * (c) LibriCore
 * 
 * Created on Oct 18, 2004
 * 
 * PhysicalDescription.java
 */
package librisuite.bean.cataloguing.bibliographic;

import librisuite.business.cataloguing.bibliographic.FixedField;
import librisuite.business.common.CorrelationValues;
import librisuite.business.common.PersistenceState;
import librisuite.business.common.PersistentObjectWithView;
import librisuite.business.common.UserViewHelper;

import com.libricore.librisuite.common.HibernateUtil;

/**
 * @author paulm
 * @version $Revision: 1.8 $, $Date: 2006/07/11 08:01:05 $
 * @since 1.0
 */
public abstract class PhysicalDescription
	extends FixedField
	implements PersistentObjectWithView {
	private char generalMaterialDesignationCode;
	private int keyNumber;
	private UserViewHelper userViewHelper = new UserViewHelper();

	/**
	 * Class constructor
	 *
	 * 
	 * @since 1.0
	 */
	public PhysicalDescription() {
		super();
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

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.FixedField#getDisplayString()
	 */
	public String getDisplayString() {
		return null;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public char getGeneralMaterialDesignationCode() {
		return generalMaterialDesignationCode;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setGeneralMaterialDesignationCode(char c) {
		generalMaterialDesignationCode = c;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getKeyNumber() {
		return keyNumber;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setKeyNumber(int i) {
		keyNumber = i;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getBibItemNumber() + getKeyNumber();
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#isAbleToBeDeleted()
	 */
	public boolean isAbleToBeDeleted() {
		return true;
	}

	protected char specificMaterialDesignationCode = 'd';
	/**
		 * 
		 * @since 1.0
		 */
	public char getSpecificMaterialDesignationCode() {
		return specificMaterialDesignationCode;
	}

	/**
		 * 
		 * @since 1.0
		 */
	public void setSpecificMaterialDesignationCode(char c) {
		specificMaterialDesignationCode = c;
	}

	/* (non-Javadoc)
	 * @see librisuite.business.cataloguing.bibliographic.Tag#correlationChangeAffectsKey(librisuite.business.common.CorrelationValues)
	 */
	/*public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1)
			&& ((v.getValue(1) < 23)
				|| (v.getValue(1) > 48)
				|| ((v.getValue(1) > 30) && (v.getValue(1) < 42)));
	}*/
	
	public boolean correlationChangeAffectsKey(CorrelationValues v) {
		return v.isValueDefined(1);
	}

	
	
	/* (non-Javadoc)
	 * @see librisuite.business.common.Persistence#getDAO()
	 */
	public HibernateUtil getDAO() {
		return getPersistenceState().getDAO();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getUserViewString() {
		return userViewHelper.getUserViewString();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setUserViewString(String string) {
		userViewHelper.setUserViewString(string);
	}

	/**
	 * 
	 * @since 1.0
	 */
	public int getBibItemNumber() {
		return getItemNumber();
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setBibItemNumber(int i) {
		setItemNumber(i);
	}

	
}
