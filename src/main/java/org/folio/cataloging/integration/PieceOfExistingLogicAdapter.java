package org.folio.cataloging.integration;

import io.vertx.core.Future;
import net.sf.hibernate.Session;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;
import org.folio.rest.impl.LogicalViewsAPI;

import java.util.Optional;

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
    final Log logger = new Log(PieceOfExistingLogicAdapter.class);

    /**
     * Executes a piece of existing logic.
     *
     * @param storageService the facade towards the cataloging persistence layer.
     * @param future the future for communicating back the outcome.
     */
    default void execute(final StorageService storageService, final Future future) {
        try {
            final Optional<T> result = Optional.ofNullable(executeAndGet(storageService,future));
            if (result.isPresent()) {
                future.complete(result.get());
            } else {
                error(PieceOfExistingLogicAdapter.class, MessageCatalog._00012_NULL_RESULT);
                future.fail(new IllegalArgumentException());
            }
        // TODO: maybe here we should provide a set of more explicit exceptions
        } catch (final Exception exception) {
            // Don't log here, the exception is supposed to be logged within the adapter.
            future.fail(exception);
        }
    }

    T executeAndGet(StorageService storageService, Future future);
}