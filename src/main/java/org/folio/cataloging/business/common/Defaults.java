package org.folio.cataloging.business.common;

import org.folio.cataloging.Global;
import org.folio.cataloging.log.Log;
import org.folio.cataloging.log.MessageCatalog;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.Optional;

/**
 * Provides access to default values established in configuration.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
// TODO all old methods have been retained for retro-compatibility purposes, but they don't have to be called!
// TODO old methods just raise an exception!
public class Defaults {
	private final static Log LOGGER = new Log(Defaults.class);

	@Deprecated
	static public String getString(String key){
		/*
		ResourceBundle defaults =
				ResourceBundle.getBundle("resources/defaultValues");

		return defaults.getString(key);
		 */
		throw new IllegalArgumentException("DON'T CALL ME!");
	}

	@Deprecated
	public static String getString(String key, String ifNotPresentValue) {
		try {
			return getString(key);
		} catch (MissingResourceException e) {
			return ifNotPresentValue;
		}
	}

	@Deprecated
	static public short getShort(String key) {
		String result = getString(key);
		return (short)Integer.parseInt(result);
	}

	@Deprecated
	static public int getInteger(String key) {
		String result = getString(key);
		return Integer.parseInt(result);
	}

	@Deprecated
	static public char getChar(String key) {
		String result = getString(key);
		return result.charAt(0);
	}

	@Deprecated
	static public Character getCharacter(String key) {
		String result = getString(key);
		return new Character(result.charAt(0));
	}

	@Deprecated
	static public boolean getBoolean(String key) {
		String result = getString(key);
		return Boolean.valueOf(result).booleanValue();
	}

	@Deprecated
	static public boolean getBoolean(String key, boolean ifNotPresentValue) {
		try {
			return getBoolean(key);
		} catch (MissingResourceException e) {
			return ifNotPresentValue;
		}
	}

	@Deprecated
	static public Class getClazz(String key) {
		String result = getString(key);
		try {
			return Class.forName(result);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

//	/**
//	 * Returns a {@link Future} holding the configuration stringValue associated with the given key.
//	 *
//	 * @param key the configuration key.
//	 * @param context the Vertx context.
//	 * @return a {@link Future} holding the configuration stringValue associated with the given key.
//
//	public static Future<String> getString(final String key, final Context context) {
//		final Future<String> future = Future.future();
//		return get(future, key, context, body -> {
//			final Optional<String> result = stringValue(body);
//			if (result.isPresent()) {
//				future.complete(result.get());
//			} else {
//				future.fail(key);
//			}
//		});
//	}
//     */
//
//	/**
//	 * Returns a {@link Future} holding the configuration stringValue associated with the given key.
//	 *
//	 * @param key the configuration key.
//	 * @param defaultValue the default stringValue (in case the key is not found).
//	 * @param context the Vertx context.
//	 * @return a {@link Future} holding the configuration stringValue associated with the given key.
//	 */
//	public static Future<String> getString(final String key, String defaultValue, final Context context) {
//		final Future<String> future = Future.future();
//		return get(future, key, context, body -> {
//			final Optional<String> result = stringValue(body);
//			future.complete(result.orElse(defaultValue));
//		});
//	}
//
//	/**
//	 * Returns a {@link Future} holding the configuration stringValue associated with the given key.
//	 *
//	 * @param key the configuration key.
//	 * @param context the Vertx context.
//	 * @return a {@link Future} holding the configuration stringValue associated with the given key.
//	 */
//	public static Future<Integer> getInteger(final String key, final Context context) {
//		final Future<Integer> future = Future.future();
//		return get(future, key, context, body -> {
//			final Optional<Integer> result = intValue(body);
//			if (result.isPresent()) {
//				future.complete(result.get());
//			} else {
//				future.fail(key);
//			}
//		});
//	}
//
//	/**
//	 * Returns a {@link Future} holding the configuration stringValue associated with the given key.
//	 *
//	 * @param key the configuration key.
//	 * @param context the Vertx context.
//	 * @return a {@link Future} holding the configuration stringValue associated with the given key.
//	 */
//	public static Future<Character> getChar(final String key, final Context context) {
//		final Future<Character> future = Future.future();
//		return get(future, key, context, body -> {
//			final Optional<Character> result = charValue(body);
//			if (result.isPresent()) {
//				future.complete(result.get());
//			} else {
//				future.fail(key);
//			}
//		});
//	}
//
//	/**
//	 * Returns a {@link Future} holding the configuration stringValue associated with the given key.
//	 *
//	 * @param key the configuration key.
//	 * @param context the Vertx context.
//	 * @return a {@link Future} holding the configuration stringValue associated with the given key.
//	 */
//	public static Future<Boolean> getBoolean(final String key, final Context context) {
//		final Future<Boolean> future = Future.future();
//		return get(future, key, context, body -> {
//			final Optional<Boolean> result = booleanValue(body);
//			if (result.isPresent()) {
//				future.complete(result.get());
//			} else {
//				future.fail(key);
//			}
//		});
//	}
//
//	/**
//	 * Returns a {@link Future} holding the configuration stringValue associated with the given key.
//	 *
//	 * @param key the configuration key.
//	 * @param defaultValue the default stringValue (in case the key is not found).
//	 * @param context the Vertx context.
//	 * @return a {@link Future} holding the configuration stringValue associated with the given key.
//	 */
//	public static Future<Boolean> getString(final String key, boolean defaultValue, final Context context) {
//		final Future<Boolean> future = Future.future();
//		return get(future, key, context, body -> {
//			final Optional<Boolean> result = booleanValue(body);
//			future.complete(result.orElse(defaultValue));
//		});
//	}
//
//	/**
//	 * Returns the configuraton proxy used by this module.
//	 *
//	 * @param context the Vertx context.
//	 * @return the configuraton proxy used by this module.
//	 */
//	private static ConfigurationsClient configuration(final Context context) {
//        return context.get(Global.CONFIGURATION_CLIENT);
//    }
//
//	/**
//	 * Internal methods used for retrieving the configuration values.
//	 *
//	 * @param future the future that will hold the stringValue.
//	 * @param key the requested configuration key.
//	 * @param context the Vertx context.
//	 * @param handler the callback handler.
//	 * @param <T> the kind of result.
//	 * @return a {@link Future} holding the configuration stringValue associated with the given key.
//	 */
//	private static <T> Future<T> get(
//			final Future<T> future,
//			final String key,
//			final Context context,
//			final Handler<Buffer> handler) {
//		try {
//			configuration(context)
//					.getEntries(
//							"module==CATALOGING and code==" + key,
//							0,
//							1,
//							"en",
//							response -> response.bodyHandler(handler));
//		} catch (final UnsupportedEncodingException exception) {
//			LOGGER.error(MessageCatalog._00015_CFG_KEY_FAILURE, exception, key);
//			future.fail(exception);
//		}
//		return future;
//	}
//
//	/**
//	 * Retrieves a configuration attribute from the given buffer.
//	 * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
//	 *
//	 * @param buffer the configuration as it comes from the mod-configuration module.
//	 * @return the configuration attribute stringValue.
//	 */
//	private static Optional<String> stringValue(final Buffer buffer) {
//		return value(buffer, obj -> obj.getString("value"));
//	}
//
//	/**
//	 * Retrieves a configuration attribute from the given buffer.
//	 * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
//	 *
//	 * @param buffer the configuration as it comes from the mod-configuration module.
//	 * @return the configuration attribute stringValue.
//	 */
//	private static Optional<Integer> intValue(final Buffer buffer) {
//		return value(buffer, obj -> obj.getInteger("value"));
//	}
//
//	/**
//	 * Retrieves a configuration attribute from the given buffer.
//	 * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
//	 *
//	 * @param buffer the configuration as it comes from the mod-configuration module.
//	 * @return the configuration attribute stringValue.
//	 */
//	private static Optional<Character> charValue(final Buffer buffer) {
//		return value(buffer, obj -> obj.getString("value").charAt(0));
//	}
//
//	/**
//	 * Retrieves a configuration attribute from the given buffer.
//	 * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
//	 *
//	 * @param buffer the configuration as it comes from the mod-configuration module.
//	 * @return the configuration attribute stringValue.
//	 */
//	private static Optional<Boolean> booleanValue(final Buffer buffer) {
//		return value(buffer, obj -> obj.getBoolean("value"));
//	}
//
//	/**
//	 * Retrieves a configuration attribute from the given buffer.
//	 * The incoming buffer is supposed to be the result of one or more calls to the mod-configuration module.
//	 *
//	 * @param buffer the configuration as it comes from the mod-configuration module.
//	 * @return the configuration attribute stringValue.
//	 */
//	private static <T> Optional<T> value(final Buffer buffer, final Function<JsonObject, T> mapper) {
//		return new JsonObject(buffer.toString())
//				.getJsonArray("configs")
//				.stream()
//				.map(JsonObject.class::cast)
//				.findFirst()
//				.map(mapper);
//	}
}