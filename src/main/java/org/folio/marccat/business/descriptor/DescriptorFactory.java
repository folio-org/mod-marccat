package org.folio.marccat.business.descriptor;

  import org.folio.marccat.business.common.AbstractMapBackedFactory;
  import org.folio.marccat.business.common.MapBackedSingletonFactory;
  import org.folio.marccat.business.common.PropertyBasedFactoryBuilder;
  import org.folio.marccat.dao.DAODescriptor;
  import org.folio.marccat.dao.persistence.Descriptor;

/**
 * Descriptor factory.
 */
public class DescriptorFactory {

  private DescriptorFactory(){}

  private static AbstractMapBackedFactory abstractMapBackedFactory;

  static {
    abstractMapBackedFactory = new MapBackedSingletonFactory();
    final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
    builder.load("/org/folio/marccat/business/cataloguing/bibliographic/descriptorFactory.properties", abstractMapBackedFactory);
    builder.load("/org/folio/marccat/business/cataloguing/bibliographic/daoFactory.properties", abstractMapBackedFactory);
  }

  public static Descriptor createDescriptor(int category) {
    return (Descriptor) abstractMapBackedFactory.create(category);
  }

  public static DAODescriptor getDao(int category) {
    return (DAODescriptor) abstractMapBackedFactory.create(category);
  }

}
