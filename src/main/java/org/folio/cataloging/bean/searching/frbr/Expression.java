package org.folio.cataloging.bean.searching.frbr;

import java.util.List;

public class Expression {
	private Integer amicusNumber;
	private Integer countManifestation;
	private List<Manifestation> manifestations;
	private String title;
	private boolean authority;
	
	public Integer getCountManifestation() {
		return countManifestation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((amicusNumber == null) ? 0 : amicusNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Expression other = (Expression) obj;
		if (amicusNumber == null) {
			if (other.amicusNumber != null)
				return false;
		} else if (!amicusNumber.equals(other.amicusNumber))
			return false;
		return true;
	}

	public void setCountManifestation(Integer countManifestation) {
		this.countManifestation = countManifestation;
	}

	public List<Manifestation> getManifestations() {
		return manifestations;
	}

	public void setManifestations(List<Manifestation> manifestations) {
		this.manifestations = manifestations;
	}

	public Integer getAmicusNumber() {
		return amicusNumber;
	}

	public void setAmicusNumber(Integer amicusNumber) {
		this.amicusNumber = amicusNumber;
	}

	public boolean isAuthority() {
		return authority;
	}

	public void setAuthority(boolean authority) {
		this.authority = authority;
	}
}
