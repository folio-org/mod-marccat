package org.folio.marccat.integration.tools;

import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.integration.TenantRefService;
import org.folio.rest.jaxrs.model.Parameter;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;


/**
 * The Class TenantLoading.
 */
@Component
public class TenantLoading {

  /** The Constant logger. */
  private static final Log logger = new Log(TenantLoading.class);

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
  List<LoadingEntry> loadingEntries ;

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
   * @param ta Tenant Attributes as they are passed via Okapi install
   * @param headers Okapi headers taken verbatim from RMBs handler
   * loaded.
   */
  public void perform(TenantAttributes ta, Map<String, String> headers) {

    String okapiUrl = headers.get("X-Okapi-Url-to");
    if (okapiUrl == null) {
      logger.debug("TenantLoading.perform No X-Okapi-Url-to header");
      okapiUrl = headers.get("X-Okapi-Url");
    }
    if (okapiUrl == null) {
      logger.debug("TenantLoading.perform No X-Okapi-Url header");
      return;
    }
    Iterator<LoadingEntry> it = loadingEntries.iterator();
    performR(okapiUrl, ta, headers, it);
  }

  /**
   * Perform R.
   *
   * @param okapiUrl the okapi url
   * @param ta the ta
   * @param headers the headers
   * @param it the it
   */
  private void performR(String okapiUrl, TenantAttributes ta,
                        Map<String, String> headers, Iterator<LoadingEntry> it) {

    LoadingEntry le = it.next();
    if (ta != null) {
      for (Parameter parameter : ta.getParameters()) {
        if (le.key.equals(parameter.getKey()) && "true".equals(parameter.getValue())) {
          loadData(okapiUrl, headers, le);
        }
      }
    }
  }

  /**
   * Load data.
   *
   * @param okapiUrl the okapi url
   * @param headers the headers
   * @param loadingEntry the loading entry
   */
  private  void loadData(String okapiUrl, Map<String, String> headers,
                         LoadingEntry loadingEntry) {
    String filePath = loadingEntry.lead;
    if (!loadingEntry.filePath.isEmpty()) {
      filePath = filePath + '/' + loadingEntry.filePath;
    }
    final String endPointUrl = okapiUrl + "/" + loadingEntry.uriPath ;
    logger.debug("Load data URL "+endPointUrl);
    try {
      List<URL> urls = getURLsFromClassPathDir(filePath);
      if (urls.isEmpty()) {
        logger.error("loadData getURLsFromClassPathDir returns empty list for path=" + filePath);
      }
      for (URL url : urls) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Global.OKAPI_TENANT_HEADER_NAME, headers.get(Global.OKAPI_TENANT_HEADER_NAME));
        httpHeaders.add(Global.OKAPI_URL, headers.get(Global.OKAPI_TENANT_HEADER_NAME));
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String,Object> requestEntity = new LinkedMultiValueMap<>();
        requestEntity.add("files", new FileSystemResource(url.getPath()));
        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<>(requestEntity, httpHeaders);
        client.exchange(
          fromUriString(endPointUrl)
            .build()
            .toUri(),
          HttpMethod.POST,
          httpEntity,
          String.class);
      }

    } catch (URISyntaxException |IOException ex) {
      logger.error("Exception for path " + filePath, ex);
    }
  }


  /**
   * Gets the UR ls from class path dir.
   *
   * @param directoryName the directory name
   * @return the UR ls from class path dir
   * @throws URISyntaxException the URI syntax exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static List<URL> getURLsFromClassPathDir(String directoryName)
    throws URISyntaxException, IOException {

    List<URL> filenames = new LinkedList<>();
    URL url = Thread.currentThread().getContextClassLoader().getResource(directoryName);
    if (url != null) {
      if (url.getProtocol().equals("file")) {
        File file = Paths.get(url.toURI()).toFile();
        if (file != null) {
          File[] files = file.listFiles();
          if (files != null) {
            for (File filename : files) {
              URL resource = filename.toURI().toURL();
              filenames.add(resource);
            }
          }
        }
      } else if (url.getProtocol().equals("jar")) {
        String dirname = directoryName + "/";
        String path = url.getPath();
        String jarPath = path.substring(5, path.indexOf('!'));
        try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
          Enumeration<JarEntry> entries = jar.entries();
          while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(dirname) && !dirname.equals(name)) {
              URL resource = Thread.currentThread().getContextClassLoader().getResource(name);
              filenames.add(resource);
            }
          }
        }
      }
    }
    return filenames;
  }

  /**
   * Adds a directory of files to be loaded (PUT/POST).
   *
   * @param filePath Relative directory path. Do not supply prefix or suffix
   * path separator (/) . The complete path is that of lead (withlead) followed
   * by this argument.
   * @param uriPath relative URI path. TenantLoading will add leading / and
   * combine with OkapiUrl.
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
