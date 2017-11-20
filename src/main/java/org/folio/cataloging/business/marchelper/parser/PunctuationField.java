package org.folio.cataloging.business.marchelper.parser;

public class PunctuationField extends PunctuationElement {

	private boolean browsable = false;
	private boolean variant = false;
	public static final char START_FIELD_SEPARATOR = '{';
	public static final char END_FIELD_SEPARATOR   = '}';

	public PunctuationField(String field) {
		super(field);
	}

	public String toString() {
		return START_FIELD_SEPARATOR + getValue() + END_FIELD_SEPARATOR;
	}

	public boolean isBrowsable() {
		return browsable;
	}

	public void setBrowsable(boolean browsable) {
		this.browsable = browsable;
	}

	public void setVariant(boolean markAsVariant) {
		variant  = markAsVariant;
	}

	public boolean isVariant() {
		return variant;
	}

}
