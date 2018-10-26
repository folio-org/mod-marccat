/*
 * Created on Sep 3, 2004
 *
 */
package org.folio.cataloging.business.common;

import java.util.Comparator;

/**
 * Class comment
 *
 * @author janick
 */
public class SubfieldCodeComparator implements Comparator {


  /**
   *
   */
  public SubfieldCodeComparator() {
    super();
  }

  private static int compareSubfielCodeString(String shortest, String longest) {
    for (int i = 0; i < shortest.length(); i++) {
      if (compareCharacter(shortest.charAt(i), longest.charAt(i)) != 0) {
        return compareCharacter(shortest.charAt(i), longest.charAt(i));
      }
    }

    if (shortest.length() == longest.length()) {
      return 0;
    } else {
      return 1;
    }
  }

  private static int compareCharacter(char c1, char c2) {
    if (Character.isDigit(c1) && !Character.isDigit(c2)) {
      return 1;
    } else if (Character.isDigit(c2) && !Character.isDigit(c1)) {
      return -1;
    } else {
      return (new Character(c1)).compareTo(new Character(c2));
    }
  }

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(Object o1, Object o2) {
    String code1 = (String) o1;
    String code2 = (String) o2;
    if (code1.length() > code2.length()) {
      return -compareSubfielCodeString(code2, code1);
    } else {
      return compareSubfielCodeString(code1, code2);
    }
  }

}
