/*
 * (c) LibriCore
 * 
 * Created on Aug 13, 2004
 * 
 * Defaults.java
 */
package org.folio.cataloging.business.common;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.folio.cataloging.Global;
import org.folio.rest.client.ConfigurationsClient;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * provides access to default values established in property pages
 * @author paulm
 * @version $Revision: 1.3 $, $Date: 2006/07/11 08:01:06 $
 * @since 1.0
 */
@Deprecated
public class Defaults {

	static public Class getClazz(String key) {
		String result = getString(key);
		try {
			return Class.forName(result);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Deprecated
	static public Future<String> getString(final String key, final Context context) {
        final Future<String> future = Future.future();
	    try {
            configuration(context)
                    .getEntries("module==CATALOGING and configName==datasource and code==" + key, 0, 1, "en",
                            response ->
                                    response.bodyHandler(body -> {
                                        final Optional<String> result = value(body);
                                        if (result.isPresent()) {
                                            future.complete(result.get());
                                        } else {
                                            future.fail(key);
                                        }
                                    }));
        } catch (UnsupportedEncodingException exception) {
	        logger.
	        future.fail(exception);
        } finally {
	        return future;
        }
	}

    /**
     * Retrieves a configuration attribute from the given buffer.
     * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
     *
     * @param buffer the configuration as it comes from the mod-configuration module.
     * @return the configuration attribute value.
     */
    public static Optional<String> value(final Buffer buffer) {
        return new JsonObject(buffer.toString())
                .getJsonArray("configs")
                .stream()
                .map(JsonObject.class::cast)
                .findFirst()
                .map(obj -> obj.getString("value"));
    }

	public static String getString(String key, String ifNotPresentValue) {
		try {
			return getString(key);
		} catch (MissingResourceException e) {
			return ifNotPresentValue;
		}
	}
	
	static public short getShort(String key) {
		String result = getString(key);
		return (short)Integer.parseInt(result);
	}

	static public int getInteger(String key) {
		String result = getString(key);
		return Integer.parseInt(result);
	}

	static public char getChar(String key) {
		String result = getString(key);
		return result.charAt(0);
	}

	static public Character getCharacter(String key) {
		String result = getString(key);
		return new Character(result.charAt(0));
	}
	@Deprecated
	static public boolean getBoolean(String key) {
		if ("labels.alphabetical.order".equals(key)) {
			return true;
		} else throw new IllegalArgumentException("DEPRECATED KEY: " + key);
/*
		String result = getString(key);
		return Boolean.valueOf(result).booleanValue();
*/
	}
	@Deprecated
	static public boolean getBoolean(String key, boolean ifNotPresentValue) {
		try {
			return getBoolean(key);
		} catch (MissingResourceException e) {
			return ifNotPresentValue;
		}
	}
	
	public static List/*<String>*/ getAllKeys(){
		ResourceBundle defaults =
			ResourceBundle.getBundle("resources/defaultValues");
		List/*<String>*/ elencoChiavi = new ArrayList/*<String>*/();
		Enumeration/*<String>*/ enm = defaults.getKeys();
		while (enm.hasMoreElements()) {
			String element = (String) enm.nextElement();
			elencoChiavi.add(element);
		}
		return elencoChiavi;
	}

	private static ConfigurationsClient configuration(final Context context) {
        return context.get(Global.CONFIGURATION_CLIENT);
    }
}
