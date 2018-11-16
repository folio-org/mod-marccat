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
    marcSourceIndicator.put(new Integer(2), new Character('0'));
    marcSourceIndicator.put(new Integer(19), new Character('1'));
    marcSourceIndicator.put(new Integer(4), new Character('2'));
    marcSourceIndicator.put(new Integer(5), new Character('3'));
    marcSourceIndicator.put(new Integer(6), new Character('4'));
    marcSourceIndicator.put(new Integer(7), new Character('5'));
    marcSourceIndicator.put(new Integer(8), new Character('6'));
    marcSourceIndicator.put(new Integer(9), new Character('7'));
  }

  public static char toMarcIndicator(int source) {
    Integer key = new Integer(source);
    if (marcSourceIndicator.containsKey(key)) {
      return marcSourceIndicator.get(key);
    } else {
      return marcSourceIndicator.get(new Integer(SOURCE_NOT_SPECIFIED));
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
