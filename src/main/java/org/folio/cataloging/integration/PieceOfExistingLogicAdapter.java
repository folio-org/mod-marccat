package org.folio.cataloging.integration;

import io.vertx.core.Future;
import net.sf.hibernate.Session;

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
public interface PieceOfExistingLogicAdapter {

    /**
     * Executes a piece of existing logic.
     *
     * @param storageService the facade towards the cataloging persistence layer.
     * @param future the future for communicating back the outcome.
     */
    void execute(StorageService storageService, Future future);
}