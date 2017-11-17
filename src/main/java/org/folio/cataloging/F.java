package org.folio.cataloging;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * Booch utility which acts as a central points for collecting static functions.
 *
 * @author Andrea Gazzarini
 * @since 1.0
 */
public abstract class F {
    /**
     * Retrieves the datasource configuration from the given buffer.
     * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
     *
     * @param value the configuration as it comes from the mod-configuration module.
     * @return the datasource configuration used within this module.
     */
    public static JsonObject datasourceConfiguration(final Buffer value) {
        return new JsonObject(value.toString())
                .getJsonArray("configs")
                .stream()
                .map(JsonObject.class::cast)
                .reduce(
                        new JsonObject(),
                        (r1, r2) -> r1.put(r2.getString("code"), r2.getValue("value")));
    }
}