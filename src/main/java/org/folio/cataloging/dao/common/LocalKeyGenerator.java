package org.folio.cataloging.dao.common;

import java.util.Hashtable;

public class LocalKeyGenerator {
  private static LocalKeyGenerator instance = null;
  private Hashtable/*<Class,Integer>*/ indexes = null;

  public LocalKeyGenerator() {
    super ( );
    indexes = new Hashtable/*<Class,Integer>*/ ( );
  }

  public static LocalKeyGenerator getInstance() {
    if (instance == null) {
      instance = new LocalKeyGenerator ( );
    }
    return instance;
  }

  public synchronized int generateNextKey(Class clz) {
    if (!indexes.containsKey (clz)) {
      indexes.put (clz, new Integer (-1));
      return -1;
    }
    int last = ((Integer) indexes.get (clz)).intValue ( );
    last--;
    indexes.put (clz, new Integer (last));
    return last;
  }
}
