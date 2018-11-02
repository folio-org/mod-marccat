package org.folio.cataloging.dao.common;

import net.sf.hibernate.MappingException;
import net.sf.hibernate.Session;
import net.sf.hibernate.mapping.Column;
import net.sf.hibernate.mapping.Component;
import net.sf.hibernate.mapping.PersistentClass;
import net.sf.hibernate.mapping.Property;
import org.folio.cataloging.Global;

import java.util.Optional;
import java.util.Spliterator;

import static java.util.Optional.ofNullable;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

public class HibernateSessionProvider {

  // TODO: Optional usage (check and fix the call hierarchy)
  public static Class getHibernateClassName(final String tableName) {
    final Spliterator <PersistentClass> iterator =
      spliteratorUnknownSize(
        Global.HCONFIGURATION.getClassMappings(),
        Spliterator.IMMUTABLE);

    return stream(iterator, false)
      .filter(clazz -> clazz.getTable().getName().equals(tableName.toUpperCase()))
      .findFirst()
      .map(PersistentClass::getMappedClass)
      .orElse(null); // TODO: Optional
  }

  /**
   * Get the columnName of the database table that is mapped to this class.
   * in the hibernate configuration
   *
   * @param clazz the class columnName.
   * @return the columnName of the database table that is mapped to this class.
   */
  // TODO: Optional usage (check and fix the call hierarchy)
  public static String getHibernateTableName(final Class clazz) {
    final PersistentClass pc = Global.HCONFIGURATION.getClassMapping(clazz);
    return pc != null ? pc.getTable().getName() : null;
  }

  /**
   * Get the columnName of the database column that is mapped to the
   * given class/property in the hibernate configuration.
   *
   * @param clazz        the owning class.
   * @param propertyName the property columnName.
   * @return the column columnName associated with the given property.
   */
  public static String getHibernateColumnName(final Class clazz, final String propertyName) {
    final Optional <PersistentClass> pclass = ofNullable(Global.HCONFIGURATION.getClassMapping(clazz));
    if (pclass.isPresent()) {
      try {
        return columnName(pclass.get().getProperty(propertyName));
      } catch (final MappingException exception) {
        final Spliterator <Property> iterator =
          spliteratorUnknownSize(
            ((Component) pclass.get().getIdentifier()).getPropertyIterator(),
            Spliterator.IMMUTABLE);

        return stream(iterator, false)
          .filter(property -> property.getName().equals(propertyName))
          .findFirst()
          .map(HibernateSessionProvider::columnName)
          .orElse(null);
      }
    }
    return null;
  }

  /**
   * Returns the column name associated with the given property.
   *
   * @param property the property.
   * @return the column name associated with the given property.
   */
  private static String columnName(final Property property) {
    return ((Column) property.getColumnIterator().next()).getName();
  }

  @Deprecated
  public void closeSession() {
    throw new IllegalArgumentException("Don't call me!");
  }

  @Deprecated
  public Session currentSession() {
    throw new IllegalArgumentException("Don't call me!");
  }
}
