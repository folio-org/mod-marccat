package org.folio.marccat.business.descriptor;

/**
 * Interface for descriptors that have a skip-in-filing indicator (Titles, Subjects)
 */
public interface SkipInFiling {

  int getSkipInFiling();

  void setSkipInFiling(int i);
}
