package org.folio.cataloging.shared;

import java.util.Arrays;

/**
 * Class representing three correlation values.
 * Each correlation is related list to select
 * headingType, itemType and functionCode (variable fields)
 * or header type code (fixed fields)
 *
 * @author janick
 * @author nbianchini
 * @author agazzarini
 * @since 1.0
 */
public class CorrelationValues implements Cloneable {

	public static final int UNDEFINED = -1;
	private int[] values = {UNDEFINED, UNDEFINED, UNDEFINED};

	/**
	 * Builds a new set of correlation values.
	 */
	public CorrelationValues() {
	}

	/**
	 * Builds a new set of correlation values.
	 *
	 * @param v1 the first correlation value.
	 * @param v2 the second correlation value.
	 * @param v3 the third correlation value.
	 */
	public CorrelationValues(final int v1, final int v2, final int v3) {
		values[0] = v1;
		values[1] = v2;
		values[2] = v3;
	}
	
	public int getValue(final int i) {
		return values[i - 1];
	}
	
	private void setValue(final int i, final int v) {
		values[i - 1] = v;
	}
	
	public boolean isValueDefined(final int i) {
		return (getValue(i) != UNDEFINED);
	}

	public CorrelationValues change(final int i, final int v) {
		final CorrelationValues val = (CorrelationValues) clone();
		val.setValue(i,v);
		return val;
	}

	/**
	 * Places the argument value in the first unused correlation slot.
	 */
	public CorrelationValues append(final int v) {
		for (int i = 0; i < values.length; i++) {
			if (!isValueDefined(i + 1)) {
				return change(i + 1, v);
			}
		}
		return this;
	}
	
	public Integer getFirstUnusedPosition() {
		for (int i = 0; i < values.length; i++) {
			if (!isValueDefined(i + 1)) {
				return i + 1;
			}
		}
		return null;
	}
	
	public Integer getLastUsedPosition() {
		for (int i = values.length - 1; i >= 0; i--) {
			if (isValueDefined(i + 1)) {
				return i + 1;
			}
		}
		return null;
	}
	
	@Override
	public Object clone() {
		final CorrelationValues v = new CorrelationValues();
		v.values = values.clone();
		return v;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() == getClass()) {
			final CorrelationValues other = (CorrelationValues) o;
			return Arrays.equals(values, other.values);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (3 * values[0]) + (5 * values[1]) + (7 * values[2]);
	}

	@Override
	public String toString() {
		return "[" + values[0] + "," + values[1] + "," + values[2] + "]";
	}
}
