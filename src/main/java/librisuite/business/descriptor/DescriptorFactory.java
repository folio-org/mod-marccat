/*
 * Created on Nov 17, 2004
 */
package librisuite.business.descriptor;

import librisuite.bean.cataloguing.heading.HeadingBean;
import librisuite.business.common.*;

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
		builder.load("/librisuite/business/descriptor/descriptorFactory.properties", descriptorFactory);
		builder.load("/librisuite/business/descriptor/daoFactory.properties", daoFactory);
		builder.load("/librisuite/business/descriptor/beanFactory.properties", beanFactory);		
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
