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

    /**
     * Adds blank spaces to the given string until it reaches the given length.
     * @param toPad	the string to pad.
     * @param padLength the padding length.
     * @return the padded string.
     */
    public static String fixedCharPadding(String toPad, int padLength) {
        toPad = toPad.trim().replaceFirst("^0+", "");
        StringBuilder builder = new StringBuilder(toPad);
        for (int index = 0 , howManyTimes = (padLength - toPad.length()); index < howManyTimes; index++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    /**
     * Returns true if the given string is not null or empty.
     *
     * @param value the input string.
     * @return true if the given string is not null or empty.
     */
    public static boolean isNotNullOrEmpty(final String value) {
        return value != null && value.trim().length() != 0;
    }
}