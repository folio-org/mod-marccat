/*
 * Created on Nov 17, 2004
 */
package librisuite.business.descriptor;

import librisuite.bean.cataloguing.heading.HeadingBean;
import librisuite.business.common.*;

/**
 * @author janick
 */
public class FunctionFactory {

	private static AbstractMapBackedFactory functionFactory;

	static {
		functionFactory = new MapBackedFactory();
		PropertyBasedFactoryBuilder builder = new PropertyBasedFactoryBuilder();
		builder.load("/librisuite/business/descriptor/functionFactory.properties", functionFactory);
	}

	public static Object createDescriptor(int category) {
		return functionFactory.create(category);
	}

	public static Object getDao(int category) {
		return  functionFactory.create(category);
	}
}
