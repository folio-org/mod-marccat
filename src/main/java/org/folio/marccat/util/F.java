package org.folio.marccat.util;

import org.folio.marccat.config.log.Log;
import org.folio.marccat.config.log.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Booch utility which acts as a central points for collecting static functions.
 *
 * @author cchiama
 * @author paulm
 * @since 1.0
 */
public abstract class F {
	private F() {
	    throw new IllegalStateException("Exception Utility class");
	  }

  private final static Log LOGGER = new Log(F.class);
  private final static String[] EMPTY_ARRAY = {};
  private final static int[] EMPTY_INT_ARRAY = {};

  /**
   * Provides a convenient way to deal with null lists, but replacing null inputs with a null-object (an empty list).
   *
   * @param values the input array.
   * @return the same input, if this is not null, otherwise an empty immutable array.
   */
  public static <T> List<T> safe(final List<T> values) {
    return values != null ? values : Collections.emptyList();
  }

  /**
   * Provides a convenient way to deal with null array, but replacing null inputs with a null-object (an empty array).
   *
   * @param values the input array.
   * @return the same input, if this is not null, otherwise an empty immutable array.
   */
  public static String[] safe(final String[] values) {
    return values != null ? values : EMPTY_ARRAY;
  }

  /**
   * Provides a convenient way to deal with null array, but replacing null inputs with a null-object (an empty array).
   *
   * @param values the input array.
   * @return the same input, if this is not null, otherwise an empty immutable array.
   */
  public static int[] safe(final int[] values) {
    return values != null ? values : EMPTY_INT_ARRAY;
  }

  /**
   * Adds blank spaces to the given string until it reaches the given length.
   *
   * @param toPad     the string to pad.
   * @param padLength the padding length.
   * @return the padded string.
   */
  public static String fixedCharPadding(String toPad, int padLength) {
    toPad = toPad.trim().replaceFirst("^0+", "");
    StringBuilder builder = new StringBuilder(toPad);
    for (int index = 0, howManyTimes = (padLength - toPad.length()); index < howManyTimes; index++) {
      builder.append(" ");
    }
    return builder.toString();
  }


  /**
   * Returns a string with leading character at begin of specified number.
   *
   * @param howManyChar -- number of leading characters to add.
   * @param number      -- the number to format.
   * @return the string representing number.
   */
  public static String padNumber(final String charToAdd, final int howManyChar, final int number) {
    return String.format("%" + charToAdd + howManyChar + "d", number);
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

  public static boolean isNotNull(final String value) {
    return value != null;
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
   * <p>
   * A later version of this class includes some minor optimizations.
   *
   * @return a copy of the object, or null if the object cannot be serialized.
   */
  public static Object deepCopy(final Object orig) {
    try (final ByteArrayOutputStream bos = new ByteArrayOutputStream();
         final ObjectOutputStream out = new ObjectOutputStream(bos)) {

      out.writeObject(orig);
      out.flush();
      out.close();

      final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
      return in.readObject();
    } catch (final Exception exception) {
      LOGGER.error(Message.MOD_MARCCAT_00013_IO_FAILURE, exception);
      return null;
    }
  }

  public static String getFormattedToday(final String formatString) {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatString);
    return LocalDateTime.now().format(formatter);
  }

  /**
   * Split string over multiple lines if exceed of specified number chars.
   *
   * @param inputString -- the input string to split.
   * @param lineSize    -- the number of chars used to split string.
   * @return list of string.
   */
  public static List<String> splitString(final String inputString, final int lineSize) {
    List<String> result = new ArrayList<>();

    Pattern p = Pattern.compile("\\b.{1," + (lineSize - 1) + "}\\b\\W?");
    Matcher m = p.matcher(inputString);

    while (m.find()) {
      result.add(m.group());
    }
    return result;
  }


}
