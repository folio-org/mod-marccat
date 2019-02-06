package org.folio.marccat.business.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link AbstractMapBackedFactory}.
 */
public class MapBackedFactory extends AbstractMapBackedFactory {

  private Map<Integer, Class> map = new HashMap<>();

  @Override
  public void put(final Integer key, final Class clazz) {
    map.put(key, clazz);
  }

  @Override
  public void put(Map<Integer, Class> entries) {
    map.putAll(entries);
  }

  @Override
  protected Object getInstance(final Integer key) {
    return newInstance(map.get(key));
  }

  @Override
  public void clear() {
    map.clear();
  }
}
