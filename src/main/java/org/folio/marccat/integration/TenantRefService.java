package org.folio.marccat.integration;

import java.util.Map;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.integration.tools.TenantLoading;
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
  public static final String SAMPLE_KEY = "loadSample";

  /** The Constant REFERENCE_KEY. */
  public static final String REFERENCE_KEY = "loadReference";

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
  public void loadData(Map<String, String> headers, boolean loadSample, boolean loadReference,
      boolean loadBibliographicSample) {
    logger.debug("Start sample data loading");
    boolean loadData = buildDataLoadingParameters(tl, loadSample, loadReference);
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
  private boolean buildDataLoadingParameters(TenantLoading tl, boolean loadSample, boolean loadReference) {
    boolean loadData = false;
    if (loadReference) {
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

}
