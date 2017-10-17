package librisuite.business.common;

import java.io.Serializable;

public class CorrelationValuesKey implements Serializable {
	short category;
	short value1;
	short value2;
	short value3;
	
	
	public CorrelationValuesKey(short category, short value1, short value2, short value3) {
		super();
		this.category = category;
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
	}
	
	public short getCategory() {
		return category;
	}
	public void setCategory(short category) {
		this.category = category;
	}
	public short getValue1() {
		return value1;
	}
	public void setValue1(short value1) {
		this.value1 = value1;
	}
	public short getValue2() {
		return value2;
	}
	public void setValue2(short value2) {
		this.value2 = value2;
	}
	public short getValue3() {
		return value3;
	}
	public void setValue3(short value3) {
		this.value3 = value3;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category;
		result = prime * result + value1;
		result = prime * result + value2;
		result = prime * result + value3;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final CorrelationValuesKey other = (CorrelationValuesKey) obj;
		if (category != other.category)
			return false;
		if (value1 != other.value1)
			return false;
		if (value2 != other.value2)
			return false;
		if (value3 != other.value3)
			return false;
		return true;
	}

}
