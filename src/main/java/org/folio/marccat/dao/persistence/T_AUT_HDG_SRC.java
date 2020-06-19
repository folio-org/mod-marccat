package org.folio.marccat.dao.persistence;

import java.util.HashMap;

public class T_AUT_HDG_SRC extends T_SINGLE {

  public static final short SOURCE_IN_SUBFIELD_2 = 9;
  public static final short SOURCE_NOT_SPECIFIED = 6;
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private static final HashMap<Integer, Character> marcSourceIndicator = new HashMap<Integer, Character>();

  static {
    marcSourceIndicator.put(2, '0');
    marcSourceIndicator.put(19, '1');
    marcSourceIndicator.put(4, '2');
    marcSourceIndicator.put(5, '3');
    marcSourceIndicator.put(6, '4');
    marcSourceIndicator.put(7, '5');
    marcSourceIndicator.put(8, '6');
    marcSourceIndicator.put(9, '7');
  }

  public static char toMarcIndicator(int source) {
    Integer key = source;
    if (marcSourceIndicator.containsKey(key)) {
      return marcSourceIndicator.get(key);
    } else {
      return marcSourceIndicator.get(SOURCE_NOT_SPECIFIED);
    }
  }

  public static short fromMarcIndicator(Character c) {
    for (Integer i : marcSourceIndicator.keySet()) {
      if (marcSourceIndicator.get(i).equals(c)) {
        return i.shortValue();
      }
    }
    return T_AUT_HDG_SRC.SOURCE_NOT_SPECIFIED;
  }
}
