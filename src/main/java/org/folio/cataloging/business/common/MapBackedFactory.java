/*
 * Created on Nov 26, 2004
 */
package org.folio.cataloging.business.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Class comment
 * @author janick
 */
public class MapBackedFactory extends AbstractMapBackedFactory {
	
	private Map map = new HashMap();

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.AbstractMapBackedFactory#put(java.lang.Object, java.lang.Class)
	 */
	public void put(Integer key, Class clazz) {
		map.put(key,clazz);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.AbstractMapBackedFactory#getInstance(java.lang.Object)
	 */
	protected Object getInstance(Integer key) {
		return newInstance((Class) map.get(key));
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.AbstractMapBackedFactory#clear()
	 */
	public void clear() {
		map.clear();
	}

}
