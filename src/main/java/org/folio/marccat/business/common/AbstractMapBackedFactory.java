package org.folio.marccat.business.common;

import java.util.Map;

import static java.util.Optional.ofNullable;

/**
 * Supertype layer for all map-backed object factories.
 */
public abstract class AbstractMapBackedFactory {
  /**
   * Clears the collected factories.
   */
  public abstract void clear();

  /**
   * Adds a new factory entry.
   *
   * @param key   the factory identifier.
   * @param clazz the product type.
   */
  public abstract void put(Integer key, Class clazz);

  /**
   * Adds all the entries within the given map.
   *
   * @param entries the factory entries.
   */
  public abstract void put(Map<Integer, Class> entries);

  /**
   * Returns an instance of the product associated with the given factory identifier.
   *
   * @param key the factory identifier / key.
   * @return an instance of the product associated with the given factory identifier.
   */
  protected abstract Object getInstance(Integer key);

  /**
   * Instantiates a new object beloging to the given type.
   *
   * @param type the object / instance type.
   * @return a new object beloging to the given type.
   */
  protected Object newInstance(final Class type) {
    return ofNullable(type).map(clazz -> {
      try {
        return clazz.newInstance();
      } catch (final Exception exception) {
        throw new IllegalArgumentException(exception);
      }
    }).orElseThrow(() -> new RuntimeException("Unable to create a valid instance of " + type));
  }

  /**
   * Returns an instance of the product associated with the given factory identifier.
   *
   * @param key the factory identifier / key.
   * @return an instance of the product associated with the given factory identifier.
   */
  public Object create(final int key) {
    return ofNullable(getInstance(key))
      .orElseThrow(() -> new RuntimeException("No Class found for key " + key));
  }
}
