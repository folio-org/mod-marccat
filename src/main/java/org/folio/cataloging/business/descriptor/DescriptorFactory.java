package org.folio.cataloging.business.descriptor;

import org.folio.cataloging.business.common.AbstractMapBackedFactory;
import org.folio.cataloging.business.common.MapBackedFactory;
import org.folio.cataloging.business.common.MapBackedSingletonFactory;
import org.folio.cataloging.business.common.PropertyBasedFactoryBuilder;
import org.folio.cataloging.dao.DAODescriptor;
import org.folio.cataloging.dao.persistence.Descriptor;

/**
 * Descriptor factory.
 *
 * @author paulm
 * @since 1.0
 */
public class DescriptorFactory {

	private static AbstractMapBackedFactory descriptorFactory;
	private static AbstractMapBackedFactory daoFactory;

	static {
		descriptorFactory = new MapBackedFactory();
		daoFactory = new MapBackedSingletonFactory();
    final PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
		builder.load("/org/folio/cataloging/business/cataloguing/bibliographic/descriptorFactory.properties", descriptorFactory);
		builder.load("/org/folio/cataloging/business/cataloguing/bibliographic/daoFactory.properties", daoFactory);
  }

	public static Descriptor createDescriptor(int category) {
		return (Descriptor) descriptorFactory.create(category);
	}

	public static DAODescriptor getDao(int category) {
		return (DAODescriptor) daoFactory.create(category);
	}

}
