package org.folio.marccat.integration;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;
import org.folio.marccat.exception.SystemInternalFailureException;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.folio.marccat.config.log.Log.error;

/**
 * Existing logic adapter.
 * As the name suggests, the goal of this class is to wrap / refactor / include / execute the existing logic
 * by using the supplied storage service, which is the interface towards the persistence layer.
 * Once a given flow will be executed, the adapter can use the provided Future for communicating back the outcome
 * (positive or negative).
 *
 * @author cchiama
 * @since 1.0
 */
@FunctionalInterface
public interface PieceOfExistingLogicAdapter<T> {
  Log logger = new Log(PieceOfExistingLogicAdapter.class);

  /**
   * Executes a piece of existing logic.
   *
   * @param storageService the facade towards the marccat persistence layer.
   * @param configuration  the configuration that has been properly loaded for this context.
   */
  @SuppressWarnings("unchecked")
  default T execute(final StorageService storageService, final Map<String, String> configuration) {
    try {
      final Optional<T> result = ofNullable(executeAndGet(storageService, configuration));
      if (result.isPresent()) {
        return result.get();
      } else {
        error(PieceOfExistingLogicAdapter.class, Message.MOD_MARCCAT_00012_NULL_RESULT);
        throw new SystemInternalFailureException(new IllegalArgumentException());
      }
    } catch (final Exception exception) {
      // Don't log here, the exception is supposed to be logged within the adapter.
      throw new SystemInternalFailureException(new IllegalArgumentException(exception));
    }
  }

  /**
   * Template method for executing the logic associated with this service.
   *
   * @param storageService the {@link StorageService} instance.
   * @param configuration  the service configuration.
   * @return the value object(s) produced by this service.
   */
  T executeAndGet(StorageService storageService, final Map<String, String> configuration);
}
