package librisuite.business.marchelper;

import librisuite.business.codetable.ValueLabelElement;

public class KeyValueLabelElement extends ValueLabelElement {
	private String key = null;
	boolean variant = false;
	
	public KeyValueLabelElement() {
		super();
	}

	public KeyValueLabelElement(String value, String label) {
		super(value, label);
	}

	public KeyValueLabelElement(int value, String label) {
		super(value, label);
	}

	public KeyValueLabelElement(short value, String label) {
		super(value, label);
	}

	public KeyValueLabelElement(String key) {
		super();
		this.key = key;
	}
	
	public KeyValueLabelElement(String key, String value, String label) {
		super(value, label);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String toString() {
		return "{" + getKey() + "}/" + getLabel() + ": '" + getValue() + "' ";
	}

	public boolean isVariant() {
		return variant;
	}

	public void setVariant(boolean variant) {
		this.variant = variant;
	}

}
