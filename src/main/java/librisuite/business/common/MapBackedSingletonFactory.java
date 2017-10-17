/*
 * Created on Nov 26, 2004
 */
package librisuite.business.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Class comment
 * @author janick
 */
public class MapBackedSingletonFactory extends AbstractMapBackedFactory {
	
	private Map map = new HashMap();
	private Map singletons = new HashMap();

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.AbstractMapBackedFactory#put(java.lang.Object, java.lang.Class)
	 */
	public void put(Integer key, Class clazz) {
		Object o = singletons.get(clazz);
		if (o == null) {
			o = newInstance(clazz);
			singletons.put(clazz, o);
		}
		map.put(key,o);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.AbstractMapBackedFactory#getInstance(java.lang.Object)
	 */
	protected Object getInstance(Integer key) {
		return map.get(key);
	}

	/* (non-Javadoc)
	 * @see librisuite.business.descriptor.AbstractMapBackedFactory#clear()
	 */
	public void clear() {
		map.clear();
		singletons.clear();
	}

}
