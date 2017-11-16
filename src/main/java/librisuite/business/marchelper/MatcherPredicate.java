package librisuite.business.marchelper;

import librisuite.business.marchelper.parser.PunctuationList;
import librisuite.business.marchelper.parser.SampleMatcher;
import librisuite.hibernate.TAG_MODEL;

import org.apache.commons.collections.Predicate;

import com.libricore.librisuite.common.StringText;

public class MatcherPredicate implements Predicate {
	private StringText stringText = null;
	private SampleMatcher matcher = new SampleMatcher();
	private String exclude = null;
	
	/**
	 * @param st StringText of the current tag
	 */
	public MatcherPredicate(StringText st) {
		super();
		this.stringText = st;
	}

	public MatcherPredicate(StringText st, String excludeCodes) {
		super();
		this.stringText = st;
		this.exclude = excludeCodes;
	}

	public boolean evaluate(Object modelObject) {
		TAG_MODEL model = (TAG_MODEL) modelObject;
		PunctuationList sample = model.getPunctuationElements();
		if(exclude!=null){
			sample = sample.getPartialList(exclude);
		}
		return matcher.deepMatch(stringText, sample);
	}

	public String toString() {
		return stringText.toString();
	}

}
