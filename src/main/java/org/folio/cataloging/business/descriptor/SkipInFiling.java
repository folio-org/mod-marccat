package org.folio.cataloging.business.descriptor;

/**
 * Interface for descriptors that have a skip-in-filing indicator (Titles, Subjects)
 * @author paulm
 * @since 1.0
 */
public interface SkipInFiling {

	int getSkipInFiling();
	
	void setSkipInFiling(int i);
}
