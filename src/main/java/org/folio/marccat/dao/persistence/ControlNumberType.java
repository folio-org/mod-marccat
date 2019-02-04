
package org.folio.marccat.dao.persistence;


public class ControlNumberType extends T_SINGLE {

  static boolean isISBN(short t) {
    return t == 9;
  }
  static boolean isISSN(short t) {
    return t == 10;
  }
  static boolean isISMN(short t) {
    return  t == 20 ||
      t == 21 ||
      t == 22;
  }
  static boolean isPublisherNumber(short t) {
    return  t == 37 ||
      t == 40 ||
      t == 43 ||
      t == 46 ||
      t == 49;
  }

}
