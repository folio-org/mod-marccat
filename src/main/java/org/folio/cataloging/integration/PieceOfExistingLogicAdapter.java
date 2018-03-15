package org.folio.cataloging.integration;

import io.vertx.core.Future;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.folio.cataloging.log.Log.error;

/**
 * Existing logic adapter.
 * As the name suggests, the goal of this class is to wrap / refactor / include / execute the existing logic
 * by using the supplied storage service, which is the interface towards the persistence layer.
 * Once a given flow will be executed, the adapter can use the provided Future for communicating back the outcome
 * (positive or negative).
 *
 * @author agazzarini
 * @since 1.0
 */
@FunctionalInterface
public interface PieceOfExistingLogicAdapter<T> {
    Log logger = new Log(PieceOfExistingLogicAdapter.class);

    /**
     * Executes a piece of existing logic.
     *
     * @param storageService the facade towards the cataloging persistence layer.
     * @param configuration the configuration that has been properly loaded for this context.
     * @param future the future for communicating back the outcome.
     */
    default void execute(final StorageService storageService, final Map<String, String> configuration, final Future future) {
        try {
            final Optional<T> result = ofNullable(executeAndGet(storageService, configuration, future));
            if (result.isPresent()) {
                future.complete(result.get());
            } else {
                error(PieceOfExistingLogicAdapter.class, MessageCatalog._00012_NULL_RESULT);
                future.fail(new IllegalArgumentException());
            }
        } catch (final Exception exception) {
            // Don't log here, the exception is supposed to be logged within the adapter.
            future.fail(exception);
        }
    }

    /**
     * Template method for executing the logic associated with this service.
     *
     * @param storageService the {@link StorageService} instance.
     * @param configuration the service configuration.
     * @param future the future associated with this execution chain.
     * @return the value object(s) produced by this service.
     */
    T executeAndGet(StorageService storageService, final Map<String, String> configuration, Future future);
}