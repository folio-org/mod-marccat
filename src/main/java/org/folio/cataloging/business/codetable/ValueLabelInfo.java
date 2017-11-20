package org.folio.cataloging.business.codetable;

public class ValueLabelInfo extends ValueLabelElement {
	private Object info = null;

	/**
	 * @return Returns the info.
	 */
	public Object getInfo() {
		return info;
	}

	/**
	 * @param info The info to set.
	 */
	public void setInfo(Object info) {
		this.info = info;
	}

	public ValueLabelInfo() {
		super();
	}

	public ValueLabelInfo(int value, String label) {
		super(value, label);
	}

	public ValueLabelInfo(short value, String label) {
		super(value, label);
	}

	public ValueLabelInfo(String value, String label) {
		super(value, label);
	} 	
	
	public ValueLabelInfo(ValueLabelElement element) {
		super(element.getValue(), element.getLabel());
	} 

}
