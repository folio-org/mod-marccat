/*
 * Created on Nov 26, 2004
 */
package org.folio.marccat.business.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton map backed factory.
 *
 * @author janick
 * @since 1.0
 */
public class MapBackedSingletonFactory extends AbstractMapBackedFactory {

  private Map<Integer, Object> map = new HashMap<>();
  private Map<Class, Object> singletons = new HashMap<>();

  @Override
  public void put(final Integer key, final Class clazz) {
    map.put(key, singletons.computeIfAbsent(clazz, k -> newInstance(k)));
  }

  @Override
  public void put(final Map<Integer, Class> entries) {
    entries.forEach((key, clazz) -> put(key, clazz));
  }

  @Override
  protected Object getInstance(final Integer key) {
    return map.get(key);
  }

  @Override
  public void clear() {
    map.clear();
    singletons.clear();
  }
}
