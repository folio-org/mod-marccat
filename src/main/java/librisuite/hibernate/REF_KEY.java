/*
 * (c) LibriCore
 * 
 * Created on Jun 15, 2004
 * 
 * NME_REF_KEY.java
 */
package librisuite.hibernate;

import java.io.Serializable;

/**
 * Represents composite key for NME_REF class
 * @author paulm
 * @version $Revision: 1.6 $, $Date: 2006/01/11 13:36:22 $
 * @since 1.0
 */
public class REF_KEY implements Serializable, Cloneable{
	private Integer source = -1;
	private Integer target = -1;
	/* The default setting for reference type should be coordinated with the default
	 * Authority tag created for category 16 in the AuthorityCatalog.tagFactory
	 */
	private Short type = 2;
	private String userViewString = "1000000000000000";

	/**
	 * override equals and hashcode for hibernate key comparison
	 */
	public boolean equals(Object anObject) {
		if (anObject instanceof REF_KEY) {
			REF_KEY aKey = (REF_KEY) anObject;
			return (
				source.equals(aKey.getSource())
					&& target.equals(aKey.getTarget())
					&& type.equals(aKey.getType())
					/*&& view.toString().matches(aKey.getView().toString())*/);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return source + target + type;
	}

	/**
	 * Getter for source
	 * 
	 * @return source
	 */
	public Integer getSource() {
		return source;
	}

	/**
	 * Getter for target
	 * 
	 * @return target
	 */
	public Integer getTarget() {
		return target;
	}

	/**
	 * Getter for type
	 * 
	 * @return type
	 */
	public Short getType() {
		return type;
	}

	/**
	 * Getter for view
	 * 
	 * @return view
	 */
	public String getUserViewString() {
		return userViewString;
	}

	/**
	 * Setter for source
	 * 
	 * @param i source
	 */
	public void setSource(Integer i) {
		source = i;
	}

	/**
	 * Setter for target
	 * 
	 * @param i target
	 */
	public void setTarget(Integer i) {
		target = i;
	}

	/**
	 * Setter for type
	 * 
	 * @param s type
	 */
	public void setType(Short s) {
		type = s;
	}

	/**
	 * Setter for view
	 * 
	 * @param s view
	 */
	public void setUserViewString(String s) {
		userViewString = s;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
