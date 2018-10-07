package org.folio.cataloging.business.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static java.lang.Class.forName;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

/**
 * TODO:
 *
 * @author janick
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class PropertyBasedFactoryBuilder {

  private static final char RANGE_INDICATOR = '-';
  private static final String NUMBER_DELIMITER = ",";
  private static final String PACKAGE_STRING_KEY = "package";

  /**
   * Loads the content of the given file (name) into the input factory.
   *
   * @param fileName the filename.
   * @param factory  the target factory.
   */
  public void load(final String fileName, final AbstractMapBackedFactory factory) {
    try {
      factory.clear ( );
      readProperties (getClass ( ).getResourceAsStream (fileName), factory);
    } catch (final IOException exception) {
      throw new RuntimeException ("ErrorCollection reading properties from stream for file " + fileName, exception);
    } catch (final ClassNotFoundException exception) {
      throw new RuntimeException ("ErrorCollection finding a class for a key in " + fileName, exception);
    }
  }

  /**
   * Loads the given properties stream into the input factory.
   *
   * @param stream  the properties stream.
   * @param factory the target factory.
   * @throws IOException            in case of I/O failure.
   * @throws ClassNotFoundException in case a property refers to a class which is not found.
   */
  private void readProperties(final InputStream stream, final AbstractMapBackedFactory factory) throws IOException, ClassNotFoundException {
    final Properties properties = new Properties ( );
    properties.load (stream);

    final String packageString = properties.getProperty (PACKAGE_STRING_KEY);
    properties.stringPropertyNames ( )
      .stream ( )
      .filter (name -> !PACKAGE_STRING_KEY.equals (name))
      .forEach (name -> mapNumbers (integerKeys (properties.getProperty (name)), findClass (packageString, name), factory));
  }

  private void mapNumbers(final List <Integer> keys, final Class clazz, final AbstractMapBackedFactory factory) {
    factory.put (
      keys.stream ( )
        .map (key -> new AbstractMap.SimpleEntry <> (key, clazz))
        .collect (toMap (AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
  }

  private List <Integer> integerKeys(final String value) {
    return stream (value.split (NUMBER_DELIMITER))
      .map (this::integersFromToken)
      .flatMap (Collection::stream)
      .collect (toList ( ));
  }

  /**
   * Produces the integer keys from the given type.
   *
   * @param token the input type.
   */
  private List <Integer> integersFromToken(final String token) {
    final int dashIndex = token.indexOf (RANGE_INDICATOR);
    return (dashIndex == -1)
      ? asList (Integer.valueOf (token))
      : range (parseInt (token.substring (0, dashIndex)), parseInt (token.substring (dashIndex + 1)))
      .mapToObj (Integer::valueOf)
      .collect (toList ( ));
  }

  private Class findClass(final String packageString, final String name) {
    try {
      return forName (ofNullable (packageString)
        .map (pack -> pack + "." + name)
        .orElse (name));
    } catch (final Exception exception) {
      throw new RuntimeException (exception);
    }
  }
}
