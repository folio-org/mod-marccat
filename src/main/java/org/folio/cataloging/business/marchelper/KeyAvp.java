package org.folio.cataloging.business.marchelper;

import org.folio.cataloging.business.codetable.Avp;

public class KeyAvp extends Avp {
	private String key = null;
	boolean variant = false;
	
	public KeyAvp() {
		super();
	}

	public KeyAvp(String value, String label) {
		super(value, label);
	}

	public KeyAvp(int value, String label) {
		super(value, label);
	}

	public KeyAvp(short value, String label) {
		super(value, label);
	}

	public KeyAvp(String key) {
		super();
		this.key = key;
	}
	
	public KeyAvp(String key, String value, String label) {
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
