/*
 * (c) LibriCore
 * 
 * Created on Jul 14, 2004
 * 
 * CollectionHelper.java
 */
package librisuite.business.common;

import java.util.Collection;
import java.util.Iterator;

/**
 * Wrapper of Collection to provide getSize method for jsp bean tags
 * 
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class CollectionHelper implements Collection {
	private Collection c;
	
	public CollectionHelper(Collection col){
		setCollection(col);
	}
	
	public int getSize() {
		return getCollection().size();
	}
	
	public boolean add(Object arg0) {
		return c.add(arg0);
	}

	public boolean addAll(Collection arg0) {
		return c.addAll(arg0);
	}

	public void clear() {
		c.clear();
	}

	public boolean contains(Object arg0) {
		return c.contains(arg0);
	}

	public boolean containsAll(Collection arg0) {
		return c.containsAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		return c.equals(arg0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return c.hashCode();
	}

	public boolean isEmpty() {
		return c.isEmpty();
	}

	public Iterator iterator() {
		return c.iterator();
	}

	public boolean remove(Object arg0) {
		return c.remove(arg0);
	}

	public boolean removeAll(Collection arg0) {
		return c.removeAll(arg0);
	}

	public boolean retainAll(Collection arg0) {
		return c.retainAll(arg0);
	}

	public int size() {
		return c.size();
	}

	public Object[] toArray() {
		return c.toArray();
	}

	public Object[] toArray(Object[] arg0) {
		return c.toArray(arg0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return c.toString();
	}

	public Collection getCollection() {
		return c;
	}

	public void setCollection(Collection collection) {
		c = collection;
	}

}
