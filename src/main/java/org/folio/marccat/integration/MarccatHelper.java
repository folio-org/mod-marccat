package org.folio.marccat.integration;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.hibernate.FlushMode;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.SystemInternalFailureException;
import org.folio.marccat.exception.UnableToCreateOrUpdateEntityException;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.StreamSupport;
import static java.util.stream.Collectors.toMap;
import static org.folio.marccat.config.constants.Global.HCONFIGURATION;

/**
 * Helper functions used within the cataloging module.
 * Specifically, this class was originally thought as a supertype layer of each resource implementor; later, it has
 * been converted in this way (i.e. a collection of static methods) because https://issues.folio.org/browse/RMB-95
 *
 * @author agazzarini
 * @since 1.0
 */
@Component("MarccatHelper")

public abstract class MarccatHelper {
  private static final  Properties DEFAULT_VALUES = new Properties();
  private static final  Map<String, DataSource> DATASOURCES = new HashMap<>();
  private static final Log logger = new Log(MarccatHelper.class);
  private static SessionFactory sessionFactory =  null;
  private static boolean testMode = false;
  private static String marccatUser;
  private static String marccatPassword;
  private static String datasourceUrl;

  public static boolean isTestMode() {
    return testMode;
  }

  public static void setTestMode(boolean testMode) {
    MarccatHelper.testMode = testMode;
  }

  public static String getMarccatUser() {
    return marccatUser;
  }

  public static void setMarccatUser(String marccatUser) {
    MarccatHelper.marccatUser = marccatUser;
  }

  public static String getMarccatPassword() {
    return marccatPassword;
  }

  public static void setMarccatPassword(String marccatPassword) {
    MarccatHelper.marccatPassword = marccatPassword;
  }


  public static String getDatasourceUrl() {
    return datasourceUrl;
  }

  public static void setDatasourceUrl(String datasourceUrl) {
    MarccatHelper.datasourceUrl = datasourceUrl;
  }


  static {
    try {
      DEFAULT_VALUES.load(MarccatHelper.class.getResourceAsStream("/defaults.properties"));
      sessionFactory =  HCONFIGURATION.buildSessionFactory();
    } catch (Exception exception) {
      throw new ExceptionInInitializerError(exception);
    }
  }

  /**
   * Executes a GET request.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param tenant            the tenant associated with the current request.
   * @param okapiUrl          the okapi url.
   * @param configurator      the configuration client.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> T doGet(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final String okapiUrl,
    final Configuration configurator,
    final String... configurationSets) {
    return exec(adapter, tenant, okapiUrl, configurator, configurationSets);
  }

  /**
   * Executes a POST request.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param tenant            the tenant associated with the current request.
   * @param okapiUrl          the okapi url.
   * @param configurator      the configuration client.
   * @param validator         a validator function for the entity associated with this resource.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> ResponseEntity<T> doPost(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final String okapiUrl,
    final Configuration configurator,
    final BooleanSupplier validator,
    final String... configurationSets) {
    if (validator.getAsBoolean()) {
      final T result = exec(adapter, tenant, okapiUrl, configurator, configurationSets);
      final HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
      return new ResponseEntity<>(result, headers, HttpStatus.CREATED);
    } else {
      throw new UnableToCreateOrUpdateEntityException();
    }
  }


  /**
   * Executes a PUT request.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param tenant            the tenant associated with the current request.
   * @param okapiUrl          the okapi url.
   * @param configurator      the configuration client.
   * @param validator         a validator function for the entity associated with this resource.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> ResponseEntity<T> doPut(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final String okapiUrl,
    final Configuration configurator,
    final BooleanSupplier validator,
    final String... configurationSets) {
    if (validator.getAsBoolean()) {
      final T result = exec(adapter, tenant, okapiUrl, configurator, configurationSets);
      final HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
      return new ResponseEntity<>(result, headers, HttpStatus.CREATED);
    } else {
      throw new UnableToCreateOrUpdateEntityException();
    }
  }



  /**
   * Executes a DELETE request.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param tenant            the tenant associated with the current request.
   * @param okapiUrl          the okapi url.
   * @param configurator      the configuration client.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> void doDelete(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final String okapiUrl,
    final Configuration configurator,
    final String... configurationSets) {
    exec(adapter, tenant, okapiUrl, configurator, configurationSets);
  }

  /**
   * Executes a DELETE request.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param tenant            the tenant associated with the current request.
   * @param okapiUrl          the okapi url.
   * @param configurator      the configuration client.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> T doDeleteWithResponse(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final String okapiUrl,
    final Configuration configurator,
    final String... configurationSets) {
    return exec(adapter, tenant, okapiUrl, configurator, configurationSets);
   }

  /**
   * Provides a unified approach (within the cataloging module) for wrapping an existing blocking flow.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param tenant            the tenant associated with the current request.
   * @param okapiUrl          the okapi url.
   * @param configurator      the configuration client.
   * @param configurationSets the configurationSets required by the current service.
   */
  private static <T> T exec(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final String okapiUrl,
    final Configuration configurator,
    final String... configurationSets) {
    try {
      final T result;
      final ObjectNode settings = configurator.attributes(tenant, okapiUrl,true, configurationSets);
      final DataSource datasource = datasource(tenant, settings);
      try (final Connection connection = datasource.getConnection();
           final StorageService service = new StorageService()) {
        Session session = sessionFactory.openSession(connection);
        session.setFlushMode(FlushMode.COMMIT);
        service.setSession(session);
        result = adapter.execute(service, configuration(service));
        service.getSession().close();
        return result;
      } catch (final SQLException exception) {
        throw new DataAccessException(exception);
      } catch (Exception exception) {
        throw new SystemInternalFailureException(exception);
      }
    } catch (Exception throwable) {
      throw new SystemInternalFailureException(throwable);
    }
  }

  /**
   * Creates a dedicated configuration for the current service.
   *
   * @param storageService the mod-configuration response.
   * @return a dedicated configuration for the current service.
   */
  private static Map<String, String> configuration(final StorageService storageService) {
       return storageService.getAllGlobalVariable();
  }
  /**
   * Retrieves the datasource configuration from the given buffer.
   * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
   *
   * @param value the configuration as it comes from the mod-configuration module.
   * @return the datasource configuration used within this module.
   */
  private static DataSource datasource(final String tenant, final ObjectNode value) {
    return DATASOURCES.computeIfAbsent(tenant, k -> newDataSourceInstance(value));
  }

  /**
   * Creates a new datasource reference.
   *
   * @return a new datasource reference.
   */
  private static DataSource newDataSourceInstance(final ObjectNode value) {
    if (!isTestMode()) {
      final Map <String, String> config = StreamSupport.stream(value.withArray("configs").spliterator(), false)
        .filter(node -> "datasource".equals(node.get("configName").asText()))
        .map(node -> new AbstractMap.SimpleEntry <>(node.get("code").asText(), node.get("value").asText()))
        .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
      logger.debug("DATABASE URL: " + config.get("url"));
      return DataSourceBuilder
        .create()
        .username(config.get("user"))
        .password(config.get("password"))
        .url(config.get("url"))
        .build();
    } else {
      return DataSourceBuilder.create().username(getMarccatUser()).password(getMarccatPassword()).url(getDatasourceUrl()).build();
    }
  }

  /**
   * A simple definition of a validation interface.
   *
   * @param <T> the kind of object that needs to be validated.
   */
  interface Valid<T> {
    Optional<T> validate(Function<T, Optional<T>> validator);
  }
}
