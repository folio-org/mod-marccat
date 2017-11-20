package org.folio.cataloging.business.marchelper;

import org.folio.cataloging.business.marchelper.parser.PunctuationList;
import org.folio.cataloging.business.marchelper.parser.SampleMatcher;
import org.folio.cataloging.dao.persistence.TAG_MODEL;

import org.apache.commons.collections.Predicate;

import org.folio.cataloging.util.StringText;

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
