package org.folio.marccat.integration.tools;

import org.apache.commons.io.IOUtils;
import org.folio.marccat.config.constants.Global;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.rest.jaxrs.model.TenantAttributes;
import org.springframework.core.io.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.net.*;
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


    loadData(okapiUrl, headers, null);
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
    final String endPointUrl = okapiUrl + "/" + "marccat/load-from-file";
    logger.debug("Load data URL " + endPointUrl);
    try {
      List <URL> urls = getURLsFromClassPathDir("sample-data/load-from-file");

      if (urls.isEmpty()) {
        logger.error("loadData getURLsFromClassPathDir returns empty list for path=" + "sample-data/load-from-file");
      }
      for (URL url : urls) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(Global.OKAPI_TENANT_HEADER_NAME, headers.get(Global.OKAPI_TENANT_HEADER_NAME));
        httpHeaders.add(Global.OKAPI_URL, headers.get(Global.OKAPI_TENANT_HEADER_NAME));
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap <String, Object> requestEntity = new LinkedMultiValueMap <>();
        File file = getResourceAsFile("/sample-data/load-from-file/record1.mrc");
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

    } catch (URISyntaxException | IOException ex) {
      logger.error("Exception for path " + "filePath", ex);
    }
  }

    /**
     * Gets the resource as a temporary file.
     *
     * @param resourcePath       the resource path
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
        logger.debug("Dirname: "+ dirname);
        String path = url.getPath();
        logger.debug("Path file: " + path);
        String jarPath = path.substring(5, path.indexOf('!'));
        logger.debug("Path jar: " + jarPath);
        try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8.name()))) {
          Enumeration<JarEntry> entries = jar.entries();
          while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.startsWith(dirname) && !dirname.equals(name)) {
              filenames.add(new URL(name));
              logger.debug("Entry jar: " + name);
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
