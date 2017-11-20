/*
 * Created on Nov 17, 2004
 */
package org.folio.cataloging.business.descriptor;

import org.folio.cataloging.bean.cataloguing.heading.HeadingBean;
import org.folio.cataloging.business.common.AbstractMapBackedFactory;
import org.folio.cataloging.business.common.MapBackedFactory;
import org.folio.cataloging.business.common.MapBackedSingletonFactory;
import org.folio.cataloging.business.common.PropertyBasedFactoryBuilder;
import org.folio.cataloging.dao.DAODescriptor;

/**
 * @author janick
 */
public class DescriptorFactory {

	private static AbstractMapBackedFactory descriptorFactory;
	private static AbstractMapBackedFactory daoFactory;
	private static AbstractMapBackedFactory beanFactory;

	static {
		descriptorFactory = new MapBackedFactory();
		daoFactory = new MapBackedSingletonFactory();
		beanFactory = new MapBackedFactory();
		PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
		builder.load("/org/folio/cataloging/business/descriptor/descriptorFactory.properties", descriptorFactory);
		builder.load("/org/folio/cataloging/business/descriptor/daoFactory.properties", daoFactory);
		builder.load("/org/folio/cataloging/business/descriptor/beanFactory.properties", beanFactory);
	}

	public static Descriptor createDescriptor(int category) {
		return (Descriptor) descriptorFactory.create(category);
	}

	public static DAODescriptor getDao(int category) {
		return (DAODescriptor) daoFactory.create(category);
	}

	public static HeadingBean createBean(int category) {
		return (HeadingBean) beanFactory.create(category);
	}

}
