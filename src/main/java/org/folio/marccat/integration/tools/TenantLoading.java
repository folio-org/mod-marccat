package org.folio.marccat.integration.tools;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

import org.apache.commons.io.IOUtils;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * The Class TenantLoading.
 */
@Component
public class TenantLoading {

  /** The Constant logger. */
  private static final Log logger = new Log(TenantLoading.class);

  /**
   * The Constant UTF_8.
   */
  public static final String UTF_8 = "UTF-8";

  /**
   * The Client.
   */
  private RestTemplate client;

  /**
   * The Enum Strategy.
   */
  private enum Strategy {

    /** The content. */
    CONTENT,
    /** The basename. */
    // Id in JSON content PUT/POST
    BASENAME,
    /** The raw put. */
    // PUT with ID as basename
    RAW_PUT,
    /** The raw post. */
    // PUT with no ID
    RAW_POST, // POST with no ID
  }

  /**
   * The Class LoadingEntry.
   */
  private class LoadingEntry {

    /** The content filter. */
    UnaryOperator<String> contentFilter;

    /** The status accept. */
    Set<Integer> statusAccept;

    /** The key. */
    String key;

    /** The lead. */
    String lead;

    /** The file path. */
    String filePath;

    /** The uri path. */
    String uriPath;

    /** The id property. */
    String idProperty;

    /** The strategy. */
    private Strategy strategy;

    /**
     * Instantiates a new loading entry.
     *
     * @param le the le
     */
    LoadingEntry(LoadingEntry le) {
      this.key = le.key;
      this.lead = le.lead;
      this.filePath = le.filePath;
      this.uriPath = le.uriPath;
      this.strategy = le.strategy;
      this.idProperty = le.idProperty;
      this.contentFilter = le.contentFilter;
      this.statusAccept = le.statusAccept;
    }

    /**
     * Instantiates a new loading entry.
     */
    LoadingEntry() {
      this.strategy = Strategy.CONTENT;
      this.idProperty = "id";
      this.contentFilter = null;
      this.statusAccept = new HashSet<>();
    }
  }

  /** The next entry. */
  LoadingEntry nextEntry;

  /** The loading entries. */
  List<LoadingEntry> loadingEntries;

  /**
   * Instantiates a new tenant loading.
   *
   * @param client the client
   */
  public TenantLoading(final RestTemplate client) {
    loadingEntries = new LinkedList<>();
    nextEntry = new LoadingEntry();
    this.client = client;
  }

  /**
   * Perform the actual loading of files
   *
   * This is normally the last method to be executed for the TenantLoading
   * instance.
   *
   * See {@link TenantLoading} for an example.
   *
   * @param headers                 Okapi headers taken verbatim from RMBs handler
   *                                loaded.
   * @param loadBibliographicSample
   * @throws IOException 
   */
  public void perform(Map<String, String> headers, boolean loadBibliographicSample) throws IOException {

    String okapiUrl = headers.get("X-Okapi-Url-to");
    if (okapiUrl == null) {
      logger.debug("TenantLoading.perform No X-Okapi-Url-to header");
      okapiUrl = headers.get("X-Okapi-Url");
    }
    if (okapiUrl == null) {
      logger.debug("TenantLoading.perform No X-Okapi-Url header");
      return;
    }
    loadData(okapiUrl, headers, loadBibliographicSample);
  }

  /**
   * Perform R.
   *
   * @param okapiUrl                the okapi url
   * @param headers                 the headers
   * @param it                      the it
   * @param loadBibliographicSample
   * @throws IOException 
   */
  private void loadData(String okapiUrl, Map<String, String> headers, boolean loadBibliographicSample) throws IOException {

    if (loadBibliographicSample) {
      loadDataBibliographic(okapiUrl, headers);
    }
    loadDataAuthority(okapiUrl, headers);
  }

  /**
   * Load data.
   *
   * @param okapiUrl the okapi url
   * @param headers  the headers
   */
  private void loadDataBibliographic(String okapiUrl, Map<String, String> headers) {
    final String endPointUrl = okapiUrl + "/" + "marccat/load-from-file";
    logger.debug("Load data URL " + endPointUrl);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(Global.OKAPI_TENANT_HEADER_NAME, headers.get(Global.OKAPI_TENANT_HEADER_NAME));
    httpHeaders.add(Global.OKAPI_URL, headers.get(Global.OKAPI_URL));
    httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    MultiValueMap<String, Object> requestEntity = new LinkedMultiValueMap<>();
    File file = getResourceAsFile("/sample-data/load-from-file/sample_records.mrc");
    requestEntity.add("files", new FileSystemResource(file));
    HttpEntity <MultiValueMap <String, Object>> httpEntity = new HttpEntity <>(requestEntity, httpHeaders);
    client.exchange(
      fromUriString(endPointUrl)
        .build()
        .toUri(),
      HttpMethod.POST,
      httpEntity,
      String.class);

  }

  /**
   * Load data.
   *
   * @param okapiUrl the okapi url
   * @param headers  the headers
   * @throws IOException 
   */
  private void loadDataAuthority(String okapiUrl, Map<String, String> headers) throws IOException {
    final String endPointUrl = okapiUrl + "/marccat/authority-record";
    logger.debug("Load data URL " + endPointUrl);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(Global.OKAPI_TENANT_HEADER_NAME, headers.get(Global.OKAPI_TENANT_HEADER_NAME));
    httpHeaders.add(Global.OKAPI_URL, headers.get(Global.OKAPI_URL));
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

    String requestJson;
    requestJson = IOUtils.toString(this.getClass().getResourceAsStream("/authority/name.json"),
        String.valueOf(StandardCharsets.UTF_8));

    HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
    String response = client.postForObject(fromUriString(endPointUrl).build().toUri(), entity, String.class);
    if ("0".equals(response)) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE);
    }

  }

  /**
   * Gets the resource as a temporary file.
   *
   * @param resourcePath the resource path
   * @return the resource as file
   */
  private File getResourceAsFile(final String resourcePath) {
    try {
      final InputStream inputStream = getClass().getResourceAsStream(resourcePath);
      if (inputStream == null) {
        return null;
      }
      final File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
      tempFile.deleteOnExit();
      String stringInputStream = IOUtils.toString(inputStream, UTF_8);
      final InputStream toInputStream = IOUtils.toInputStream(stringInputStream, UTF_8);
      IOUtils.copy(toInputStream, new FileOutputStream(tempFile));
      return tempFile;
    } catch (IOException exception) {
      logger.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
      return null;
    }

  }

  /**
   * Adds a directory of files to be loaded (PUT/POST).
   *
   * @param filePath Relative directory path. Do not supply prefix or suffix path
   *                 separator (/) . The complete path is that of lead (withlead)
   *                 followed by this argument.
   * @param uriPath  relative URI path. TenantLoading will add leading / and
   *                 combine with OkapiUrl.
   * @return TenantLoading new state
   */
  public TenantLoading add(String filePath, String uriPath) {
    nextEntry.filePath = filePath;
    nextEntry.uriPath = uriPath;
    loadingEntries.add(new LoadingEntry(nextEntry));
    return this;
  }

  /**
   * With key.
   *
   * @param key the key
   * @return the tenant loading
   */
  public TenantLoading withKey(String key) {
    this.nextEntry.key = key;
    return this;
  }

  /**
   * With lead.
   *
   * @param lead the lead
   * @return the tenant loading
   */
  public TenantLoading withLead(String lead) {
    this.nextEntry.lead = lead;
    return this;
  }

}
