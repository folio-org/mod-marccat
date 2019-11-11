package org.folio.marccat.integration;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Configuration {
  /**
   * Returns the configuration associated with the given tenant and belonging to the input sets (groups).
   *
   * @param tenant            the tenant identifier.
   * @param okapiUrl          the okapi url.
   * @param configurationSets the attributes group(s).
   * @return the configuration associated with the given tenant and belonging to the input sets (groups).
   */
  ObjectNode attributes(String tenant, String okapiUrl, boolean withDatasource, String... configurationSets);
}
