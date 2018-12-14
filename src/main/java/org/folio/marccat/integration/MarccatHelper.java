package org.folio.marccat.integration;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.folio.marccat.exception.DataAccessException;
import org.folio.marccat.exception.SystemInternalFailureException;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;
import static org.folio.marccat.config.Global.HCONFIGURATION;

/**
 * Helper functions used within the marccat module.
 * Specifically, this class was originally thought as a supertype layer of each resource implementor; later, it has
 * been converted in this way (i.e. a collection of static methods) because https://issues.folio.org/browse/RMB-95
 *
 * @author cchiama
 * @since 1.0
 */
public abstract class MarccatHelper {
  private final static Properties DEFAULT_VALUES = new Properties();
  private final static Map<String, DataSource> DATASOURCES = new HashMap<>();

  static {
    try {
      DEFAULT_VALUES.load(MarccatHelper.class.getResourceAsStream("/defaults.properties"));
    } catch (final Throwable exception) {
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
   * Provides a unified approach (within the marccat module) for wrapping an existing blocking flow.
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
      } catch (final Throwable exception) {
        throw new SystemInternalFailureException(exception);
      }
    } catch (final Throwable throwable) {
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
    return StreamSupport
      .stream(value.withArray("configs").spliterator(), false)
      .filter(node -> !"datasource".equals(node.get("configName").asText()))
      .map(node -> new AbstractMap.SimpleEntry<>(node.get("code").asText(), node.get("value").asText()))
      .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
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
    final Map<String, String> config = StreamSupport.stream(value.withArray("configs").spliterator(), false)
      .filter(node -> "datasource".equals(node.get("configName").asText()))
      .map(node -> new AbstractMap.SimpleEntry<>(node.get("code").asText(), node.get("value").asText()))
      .collect(toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    return DataSourceBuilder
      .create()
      .username(config.get("user"))
      .password(config.get("password"))
      .url(config.get("url"))
      .build();
  }

}
