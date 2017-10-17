/*
 * Created on Nov 17, 2004
 */
package librisuite.business.common;

import java.util.Arrays;

/**
 * @author janick
 *
 */
public class CorrelationValues implements Cloneable {
	public static final short UNDEFINED = -1;
	private static final int NBR_VALUES = 3;
	
	private short[] values = new short[NBR_VALUES];
  


	/**
	 * 
	 */
	public CorrelationValues() {
		super();
		for (int i = 0; i < NBR_VALUES; i++) {
			values[i] = UNDEFINED;
		}
	}
	
	public CorrelationValues(short v1, short v2, short v3) {
		values[0] = v1;
		values[1] = v2;
		values[2] = v3;
	}
	
	public short getValue(int i) {
		checkIfInRange(i);
		return values[i-1];
	}
	
	private void setValue(int i, short v) {
		checkIfInRange(i);
		values[i-1] = v;
	}
	
	public boolean isValueDefined(int i) {
		return (getValue(i) != UNDEFINED);
	}

	/**
	 * @param i
	 */
	private void checkIfInRange(int i) {
		if ((i <= 0) || (i > NBR_VALUES)) {
			throw new IllegalArgumentException(
				"There are only "
					+ NBR_VALUES
					+ " correlation values, value "
					+ i
					+ " is out of range");
		}
	}

	public CorrelationValues change(int i, short v) {
		CorrelationValues val = (CorrelationValues) this.clone();
		val.setValue(i,v);
		return val;
	}

	/**
	 * places the argument value in the first unused correlation slot
	 * 
	 * @since 1.0
	 */
	public CorrelationValues append(short v) {
		for (int i=0; i<values.length; i++) {
			if (!isValueDefined(i+1)) {
				return change(i+1,v);
			}
		}
		return this;
	}
	
	public Integer getFirstUnusedPosition() {
		for (int i=0; i<values.length; i++) {
			if (!isValueDefined(i+1)) {
				return new Integer(i+1);
			}
		}
		return null;
	}
	
	public Integer getLastUsedPosition() {
		for (int i = values.length - 1; i >= 0; i--) {
			if (isValueDefined(i+1)) {
					return new Integer(i + 1);
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		CorrelationValues v = new CorrelationValues();
		v.values = (short[]) values.clone();
		return v;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o.getClass() == getClass()) {
			CorrelationValues other = (CorrelationValues) o;
			return Arrays.equals(values, other.values);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (3 * values[0]) + (5 * values[1]) + (7 * values[2]);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + values[0] + "," + values[1] + "," + values[2] + "]";
	}

}
