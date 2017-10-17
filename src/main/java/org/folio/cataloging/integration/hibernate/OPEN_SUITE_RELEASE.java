package org.folio.cataloging.integration.hibernate;

import java.io.Serializable;

public class OPEN_SUITE_RELEASE implements Serializable{
	
	private static final long serialVersionUID = -1699651704083793713L;
	
	int releaseNumber;
	int servicePackNumber;

	public int getReleaseNumber() {
		return releaseNumber;
	}
	public void setReleaseNumber(int releaseNumber) {
		this.releaseNumber = releaseNumber;
	}
	
	public int getServicePackNumber() {
		return servicePackNumber;
	}
	public void setServicePackNumber(int servicePackNumber) {
		this.servicePackNumber = servicePackNumber;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + releaseNumber;
		result = prime * result + servicePackNumber;
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OPEN_SUITE_RELEASE other = (OPEN_SUITE_RELEASE) obj;
		if (releaseNumber != other.releaseNumber)
			return false;
		if (servicePackNumber != other.servicePackNumber)
			return false;
		return true;
	}
}
