package librisuite.business.marchelper.parser;

public class PunctuationElement {

	protected String value;

	public PunctuationElement(String punctuation) {
		this.value = punctuation;
	}

	public PunctuationElement() {
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}

}
