
/*
 * (c) LibriCore
 * 
 * Created on Jul 20, 2004
 * 
 * ValueLabelElement.java (was CodeTableCharListElement.java)
 */
package librisuite.business.codetable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Holds a single entry for select/option lists where the valueProperty is a String
 * @author paulm
 * @version %I%, %G%
 * @since 1.0
 */
public class ValueLabelElement implements Comparable, Serializable {
	private String value;
	private String label;
	
	public static String decode(String value, List list) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			ValueLabelElement anElem = (ValueLabelElement) iter.next();
			if (anElem.getValue().equals(value)) {
				return anElem.getLabel();
			}
		}
		return null;
	}
	
	public ValueLabelElement() {
	}
	
	/**
	 * 
	 * Class constructor
	 *
	 * @param value
	 * @param label
	 * @since 1.0
	 */
	public ValueLabelElement(String value, String label){
		setValue(value);
		setLabel(label);
	}
	
	/**
	 * 
	 * Class constructor
	 *
	 * @param value
	 * @param label
	 * @since 1.0
	 */
	public ValueLabelElement(int value, String label){
		setValue(String.valueOf(value));
		setLabel(label);
	}
	
	/**
	 * 
	 * Class constructor
	 *
	 * @param value
	 * @param label
	 * @since 1.0
	 */
	public ValueLabelElement(short value, String label){
		setValue(String.valueOf(value));
		setLabel(label);
	}
	
	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	public void setLabel(String string) {
		label = string;
	}

	public void setValue(String string) {
		value = string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ValueLabelElement) {
			ValueLabelElement anElem = (ValueLabelElement)obj;
			return this.getValue().equals(anElem.getValue()) &&
					this.getLabel().equals(anElem.getLabel());
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (getValue() + getLabel()).hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * This "natural" order is based on alphabetic by label
	 */
	public int compareTo(Object o) {
		return this.getLabel().compareTo(((ValueLabelElement)o).getLabel());
	}

	public String toString() {
		return "("+ getValue() + "," + getLabel() + ")";
	}

}
