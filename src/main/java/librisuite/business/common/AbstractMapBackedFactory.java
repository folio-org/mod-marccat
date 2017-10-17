/*
 * Created on Nov 26, 2004
 */
package librisuite.business.common;


/**
 * Class comment
 * @author janick
 */
public abstract class AbstractMapBackedFactory {
	
	public abstract void clear();
	public abstract void put(Integer key, Class clazz);
	protected abstract Object getInstance(Integer key);

	protected Object newInstance(Class clazz) {
		if (clazz == null) return null;
		try {
			return (clazz).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(
				"Could not get a new instance of " + clazz.getName(),
				e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
				"Could not get a new instance of " + clazz.getName(),
				e);
		}
	}
	

	public Object create(int key) {
		Object o = getInstance(new Integer(key));
		if (o != null) {
			return o;
		} else {
			throw new RuntimeException("No Class found for key " + key );
		}
	}	
}
