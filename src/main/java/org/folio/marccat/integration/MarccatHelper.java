package org.folio.marccat.integration;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.marccat.config.log.Log;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.SystemInternalFailureException;
import org.folio.marccat.exception.UnableToCreateOrUpdateEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
  private final static Properties DEFAULT_VALUES = new Properties();
  private final static Map<String, DataSource> DATASOURCES = new HashMap<>();
  private static final Log logger = new Log(MarccatHelper.class);

  /**
   * The test mode for integration test.
   */
  public static boolean testMode = false;

  /**
   * The marccat user.
   */
  public static String marccatUser;

  /**
   * The marccat password .
   */
   public static String marccatPassword;

  /**
   * The datasource url .
   */
  public static String datasourceUrl;


  static {
    try {
      DEFAULT_VALUES.load(MarccatHelper.class.getResourceAsStream("/defaults.properties"));
     } catch (Exception exception) {
      throw new ExceptionInInitializerError(exception);
    }
  }

  /**
   * Executes a GET request.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param tenant            the tenant associated with the current request.
   * @param configurator      the configuration client.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> T doGet(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final Configuration configurator,
    final String... configurationSets) {
    return exec(adapter, tenant, configurator, configurationSets);
  }

  /**
   * Executes a POST request.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param tenant            the tenant associated with the current request.
   * @param configurator      the configuration client.
   * @param validator         a validator function for the entity associated with this resource.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> ResponseEntity<T> doPost(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final Configuration configurator,
    final BooleanSupplier validator,
    final String... configurationSets) {
    if (validator.getAsBoolean()) {
      final T result = exec(adapter, tenant, configurator, configurationSets);
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
   * @param configurator      the configuration client.
   * @param validator         a validator function for the entity associated with this resource.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> ResponseEntity<T> doPut(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final Configuration configurator,
    final BooleanSupplier validator,
    final String... configurationSets) {
    if (validator.getAsBoolean()) {
      final T result = exec(adapter, tenant, configurator, configurationSets);
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
   * @param configurator      the configuration client.
   * @param configurationSets the requested configuration attributes sets.
   */
  public static <T> void doDelete(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final Configuration configurator,
    final String... configurationSets) {
    exec(adapter, tenant, configurator, configurationSets);
  }

  /**
   * Provides a unified approach (within the cataloging module) for wrapping an existing blocking flow.
   *
   * @param adapter           the bridge that carries on the existing logic.
   * @param configurationSets the configurationSets required by the current service.
   */
  private static <T> T exec(
    final PieceOfExistingLogicAdapter<T> adapter,
    final String tenant,
    final Configuration configurator,
    final String... configurationSets) {
    try {

      final ObjectNode settings = configurator.attributes(tenant, true, configurationSets);
      final DataSource datasource = datasource(tenant, settings);
      try (final Connection connection = datasource.getConnection();
           final StorageService service =
             new StorageService(
               HCONFIGURATION.buildSessionFactory().openSession(connection))) {
        return adapter.execute(service, configuration(settings));
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
   * @param value the mod-configuration response.
   * @return a dedicated configuration for the current service.
   */
  private static Map<String, String> configuration(final ObjectNode value) {
    //TODO 23/06 legge i dati dalla SYS_GBL_VRBL
   /* return StreamSupport.stream(value.withArray("configs").spliterator(), false)
      .filter(node -> !"datasource".equals(node.get("configName").asText()))
      .filter(node -> node.get("code") != null && node.get("value") != null)
      .map(node -> new AbstractMap.SimpleEntry<>(node.get("code").asText(), node.get("value").asText()))
      .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));*/
   return new HashMap<String, String>() ;
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
    System.out.println("02 TEST here: " + testMode);
    if (!testMode) {
      final Map <String, String> config = StreamSupport.stream(value.withArray("configs").spliterator(), false)
        .filter(node -> "datasource".equals(node.get("configName").asText()))
        .map(node -> new AbstractMap.SimpleEntry <>(node.get("code").asText(), node.get("value").asText()))
        .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
      return DataSourceBuilder
        .create()
        .username(config.get("user"))
        .password(config.get("password"))
        .url(config.get("url"))
        .build();
    } else {
      return DataSourceBuilder.create().username(marccatUser).password(marccatPassword).url(datasourceUrl).build();
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
