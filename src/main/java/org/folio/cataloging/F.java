package org.folio.cataloging;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

/**
 * Booch utility which acts as a central points for collecting static functions.
 *
 * @author agazzarini
 * @author paulm
 * @since 1.0
 */
public abstract class F {
    private final static Log LOGGER = new Log(F.class);

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
                        (r1, r2) -> r1.put(r2.getString("code"), r2.getValue("stringValue")));
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

    /**
     * Returns the locale associated with the given code.
     *
     * @param code the locale code.
     * @return the locale associated with the given code, Locale#ENGLISH in case the code is empty or null.
     */
    public static Locale locale(final String code) {
        return isNotNullOrEmpty(code) ? Locale.forLanguageTag(code) : Locale.ENGLISH;
    }

    /**
     * Utility for making deep copies (vs. clone()'s shallow copies) of  objects.
     * Objects are first serialized and then deserialized. Error
     * checking is fairly minimal in this implementation. If an object is
     * encountered that cannot be serialized (or that references an object
     * that cannot be serialized) an error is printed to System.err and
     * null is returned. Depending on your specific application, it might
     * make more sense to have copy(...) re-throw the exception.
     *
     * A later version of this class includes some minor optimizations.
     *
     * @return a copy of the object, or null if the object cannot be serialized.
     */
    public static Object deepCopy(final Object orig) {
        try(final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream out = new ObjectOutputStream(bos)) {

            out.writeObject(orig);
            out.flush();
            out.close();

            final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return in.readObject();
        }
        catch(final Exception exception) {
            LOGGER.error(MessageCatalog._00013_IO_FAILURE, exception);
            return null;
        }
    }

    /**
     * Returns the string version of the incoming object, or null (if the object is null).
     *
     * @param obj the input.
     * @return the string version of the incoming object, or null.
     */
    public static String asNullableString(final Object obj) {
        return (obj != null) ? String.valueOf(obj) : null;
    }

    /**
     *
     * @param s
     * @return
     */
    public static Character characterFromXML(String s) {
        if (s.length() == 0) {
            return null;
        }
        else {
            return new Character(s.charAt(0));
        }
    }

    public static String stringFromXML(String s) {
        if (s.length() == 0) {
            return null;
        }
        else {
            return s;
        }
    }
}