package org.folio.cataloging.business.codetable;

import java.io.Serializable;
import java.util.List;

/**
 * Holds a single entry for select/option lists where the valueProperty is a String.
 *
 * @author paulm
 * @author agazzarini
 * @since 1.0
 */
public class ValueLabelElement implements Comparable, Serializable {
	private String value;
	private String label;

	/**
	 * Decodes the incoming value among a given set of key/value pairs.
	 *
	 * @param value the search criterion.
	 * @param elements the search set.
	 * @return the label associated with the matching element, null otherwise.
	 */
	public static String decode(final String value, final List<ValueLabelElement> elements) {
		return elements.stream()
				.filter(element -> element.getValue().equals(value))
				.findFirst()
				.map(ValueLabelElement::getLabel)
				.orElse(null);
	}
	
	public ValueLabelElement() {}

	/**
	 * Builds a new {@link ValueLabelElement} with the given pair.
	 *
	 * @param value the element value.
	 * @param label the element label.
	 */
	public ValueLabelElement(final String value, final String label){
		setValue(value);
		setLabel(label);
	}

	/**
	 * Builds a new {@link ValueLabelElement} with the given pair.
	 *
	 * @param value the element value.
	 * @param label the element label.
	 */
	public ValueLabelElement(int value, String label){
		setValue(String.valueOf(value));
		setLabel(label);
	}

	/**
	 * Builds a new {@link ValueLabelElement} with the given pair.
	 *
	 * @param value the element value.
	 * @param label the element label.
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

	public void setLabel(final String label) {
		this.label = label;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof ValueLabelElement)
				&& ((ValueLabelElement)obj).value.equals(value)
				&& ((ValueLabelElement)obj).label.equals(label);
	}

	@Override
	public int hashCode() {
		return (getValue() + getLabel()).hashCode();
	}

	@Override
	public int compareTo(final Object o) {
		return this.getLabel().compareTo(((ValueLabelElement)o).getLabel());
	}

	@Override
	public String toString() {
		return new StringBuilder("(")
				.append(getValue())
				.append(",")
				.append(getLabel())
				.append(")")
				.toString();
	}
}
