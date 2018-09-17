/*
 * (c) LibriCore
 * 
 * Created on Jun 16, 2004
 * 
 * MassagedCrossReferences.java
 */
package org.folio.cataloging.business.crossreference;

import org.folio.cataloging.dao.persistence.Descriptor;
import org.folio.cataloging.dao.persistence.REF;


/**
 * A data class to simplify access to cross references for presentation
 * @author paulm
 */
public class CrossReferenceSummaryElement 
{
	private String decodedType;
	private String decodedPrintConstant;
	private String target;
	private REF XRef;
	private int docTargetCounts;
	private Descriptor targetDescriptor;
	
	public Descriptor getTargetDescriptor() {
		return targetDescriptor;
	}

	public void setTargetDescriptor(Descriptor targetDescriptor) {
		this.targetDescriptor = targetDescriptor;
	}

	public int getDocTargetCounts() {
		return docTargetCounts;
	}

	public void setDocTargetCounts(int docTargetCounts) {
		this.docTargetCounts = docTargetCounts;
	}

	/**
	 * gets the text description of the reference type
	 * 
	 * @return the text
	 */
	public String getDecodedType() {
		return decodedType;
	}

	/**
	 * gets the Target heading's stringText
	 * 
	 * @return the text
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * sets the text of the decoded reference type
	 * 
	 * @param string the text to be used
	 */
	public void setDecodedType(String string) {
		decodedType = string;
	}

	/**
	 * sets the target heading's string text (for this cross-reference)
	 * 
	 * @param text the text to be used
	 */
	public void setTarget(String text) {
		target = text;
	}


	public REF getXRef() {
		return XRef;
	}

	public void setXRef(REF ref) {
		XRef = ref;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public String getDecodedPrintConstant() {
		return decodedPrintConstant;
	}

	/**
	 * 
	 * @since 1.0
	 */
	public void setDecodedPrintConstant(String string) {
		decodedPrintConstant = string;
	}

}
