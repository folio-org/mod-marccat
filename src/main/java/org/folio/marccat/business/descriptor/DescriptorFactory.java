package org.folio.marccat.business.descriptor;

import org.folio.marccat.business.common.AbstractMapBackedFactory;
import org.folio.marccat.business.common.MapBackedSingletonFactory;
import org.folio.marccat.business.common.PropertyBasedFactoryBuilder;
import org.folio.marccat.dao.DescriptorDAO;
import org.folio.marccat.dao.persistence.Descriptor;

/**
 * Descriptor factory.
 */
public class DescriptorFactory {

  private static AbstractMapBackedFactory abstractMapBackedFactory;

  static {
    abstractMapBackedFactory = new MapBackedSingletonFactory();
    final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
    builder.load("/org/folio/marccat/business/cataloguing/bibliographic/descriptorFactory.properties", abstractMapBackedFactory);
    builder.load("/org/folio/marccat/business/cataloguing/bibliographic/daoFactory.properties", abstractMapBackedFactory);
  }

  private DescriptorFactory() {
  }

  public static Descriptor createDescriptor(int category) {
    return (Descriptor) abstractMapBackedFactory.create(category);
  }

  public static DescriptorDAO getDao(int category) {
    return (DescriptorDAO) abstractMapBackedFactory.create(category);
  }

}
