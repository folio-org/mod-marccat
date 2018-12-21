package org.folio.marccat.business.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton map backed factory.
 *
 * @author paulm
 * @since 1.0
 */
public class MapBackedSingletonFactory extends AbstractMapBackedFactory {

  /** The map. */
  private Map<Integer, Object> map = new HashMap<>();

  /** The singletons. */
  private Map<Class, Object> singletons = new HashMap<>();

  /**
   * Put.
   *
   * @param key the key
   * @param clazz the clazz
   */
  @Override
  public void put(final Integer key, final Class clazz) {
    map.put(key, singletons.computeIfAbsent(clazz, k -> newInstance(k)));
  }

  /**
   * Put.
   *
   * @param entries the entries
   */
  @Override
  public void put(final Map<Integer, Class> entries) {
    entries.forEach((key, clazz) -> put(key, clazz));
  }

  /**
   * Gets the single instance of MapBackedSingletonFactory.
   *
   * @param key the key
   * @return single instance of MapBackedSingletonFactory
   */
  @Override
  protected Object getInstance(final Integer key) {
    return map.get(key);
  }

  /**
   * Clear.
   */
  @Override
  public void clear() {
    map.clear();
    singletons.clear();
  }
}
