package org.folio.marccat.integration;

import java.util.List;
import java.util.Map;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.integration.tools.TenantLoading;
import org.folio.rest.jaxrs.model.Parameter;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class TenantRefService.
 */
@Service("TenantRefService")
public class TenantRefService {

  private static final Log logger = new Log(TenantRefService.class);

  /** The Constant SAMPLE_LEAD. */
  private static final String SAMPLE_LEAD = "sample-data";

  /** The Constant SAMPLE_KEY. */
  private static final String SAMPLE_KEY = "loadSample";

  /** The Constant REFERENCE_KEY. */
  private static final String REFERENCE_KEY = "loadReference";

  /** The Constant REFERENCE_LEAD. */
  private static final String REFERENCE_LEAD = "ref-data";
  /**
   * The remote configuration.
   */
  @Autowired
  private TenantLoading tl;

  /**
   * Load data.
   *
   * @param tenantAttributes        the tenant attributes
   * @param headers                 the headers
   * @param loadBibliographicSample
   */
  public void loadData(TenantAttributes tenantAttributes, Map<String, String> headers, boolean loadSample,
      boolean loadBibliographicSample) {
    logger.debug("Start sample data loading");
    boolean loadData = buildDataLoadingParameters(tenantAttributes, tl, loadSample);
    logger.debug("Is Load data " + loadData);
    // if (loadData) {
    tl.perform(headers, loadBibliographicSample);
    // }
    logger.debug("End sample data loading");
  }

  /**
   * Builds the data loading parameters.
   *
   * @param tenantAttributes        the tenant attributes
   * @param tl                      the tl
   * @param loadSample
   * @param loadBibliographicSample
   * @return true, if successful
   */
  private boolean buildDataLoadingParameters(TenantAttributes tenantAttributes, TenantLoading tl, boolean loadSample) {
    boolean loadData = false;
    if (isLoadReference(tenantAttributes)) {
      tl.withKey(REFERENCE_KEY).withLead(REFERENCE_LEAD);
      loadData = true;
    }
    if (loadSample) {
      tl.withKey(SAMPLE_KEY).withLead(SAMPLE_LEAD);
      tl.add("load-from-file", "marccat/load-from-file");
      loadData = true;
    }
    logger.debug("Load data = " + loadData);
    return loadData;
  }

  /**
   * Checks if is load reference.
   *
   * @param tenantAttributes the tenant attributes
   * @return true, if is load reference
   */
  private boolean isLoadReference(TenantAttributes tenantAttributes) {
    boolean loadReference = false;
    List<Parameter> parameters = tenantAttributes.getParameters();
    for (Parameter parameter : parameters) {
      if (REFERENCE_KEY.equals(parameter.getKey())) {
        loadReference = Boolean.parseBoolean(parameter.getValue());
      }
    }
    return loadReference;

  }

  /**
   * Checks if is load sample.
   *
   * @param tenantAttributes the tenant attributes
   * @return true, if is load sample
   */
  private boolean isLoadSample(TenantAttributes tenantAttributes) {
    boolean loadSample = false;
    List<Parameter> parameters = tenantAttributes.getParameters();
    for (Parameter parameter : parameters) {
      logger.debug("Load Sample Parameter " + parameter.getKey());
      logger.debug("Load Sample Value " + parameter.getValue());
      if (SAMPLE_KEY.equals(parameter.getKey())) {
        loadSample = Boolean.parseBoolean(parameter.getValue());
      }
    }
    return loadSample;

  }

}
