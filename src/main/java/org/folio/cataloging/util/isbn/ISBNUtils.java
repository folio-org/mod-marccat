package org.folio.cataloging.util.isbn;

import java.util.regex.Pattern;

/**
 * @author Christian Chiama
 * @version $Revision: 4 $ $Date: 2018-10-1
 */
public class ISBNUtils {

  private static String REGEX = "[^\\dX]";
  private static Pattern NON_ISBN_CHARACTERS = Pattern.compile(REGEX);


  /**
   * @param ISBN
   * @return
   */
  static String removeHyphen(String ISBN) {
    return ISBN.replaceAll(GlobalConst.HYPHENS, GlobalConst.NO_SPACE);
  }

  /**
   * @param ISBN
   * @param character
   * @return
   */
  static String removeChar(String ISBN, String character) {
    return ISBN.replaceAll(character, GlobalConst.NO_SPACE);
  }

  /**
   * @param ISBN
   * @return
   */
  static String stripChar(String ISBN) {
    return NON_ISBN_CHARACTERS.matcher(ISBN).replaceAll(GlobalConst.NO_SPACE);
  }

  /**
   * @param str
   * @param width
   * @param filler
   * @return
   */
  static String rightAlign(String str, int width, char filler) {
    while (str.length() < width) {
      str += filler;
    }
    return str;
  }
}
