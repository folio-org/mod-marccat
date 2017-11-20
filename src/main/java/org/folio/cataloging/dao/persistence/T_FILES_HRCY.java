package org.folio.cataloging.dao.persistence;

import java.io.Serializable;

public class T_FILES_HRCY implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String natureLevel;
	String parentNatureLevel;
	
	public String getNatureLevel() {
		return natureLevel;
	}
	public void setNatureLevel(String natureLevel) {
		this.natureLevel = natureLevel;
	}
	public String getParentNatureLevel() {
		return parentNatureLevel;
	}
	public void setParentNatureLevel(String parentNatureLevel) {
		this.parentNatureLevel = parentNatureLevel;
	}
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((natureLevel == null) ? 0 : natureLevel.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		T_FILES_HRCY other = (T_FILES_HRCY) obj;
		if (natureLevel == null) {
			if (other.natureLevel != null)
				return false;
		} else if (!natureLevel.equals(other.natureLevel))
			return false;
		return true;
	}


}
