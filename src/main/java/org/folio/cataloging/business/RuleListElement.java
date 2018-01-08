package org.folio.cataloging.business;

import org.folio.cataloging.dao.persistence.CLCTN_MST_RULE;

// TODO: Javadoc
public class RuleListElement {
	CLCTN_MST_RULE rule;
	private int ruleId;
	private String ruleDescription;
	private String level;
	private String dataType;
	private String dataRange;
	private String dataUploadFrom;
	private String dataUploadTo;
	private String dataInsert;
	private String dataUpdate;
	private String dataProcessing;
	private String flagProcessing;
	private String collSource;
	
	public RuleListElement(final CLCTN_MST_RULE rule) {
		this.rule = rule;
		this.ruleId = rule.getRuleId();
		this.flagProcessing = rule.getFlagProcessing();
		this.ruleDescription = rule.getRuleDescription();
		this.dataType = rule.getDataType();
	}

	public CLCTN_MST_RULE getRule() {
		return rule;
	}

	public void setCollSource(String collSource) {
		this.collSource = collSource;
	}

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setDataRange(String dataRange) {
		this.dataRange = dataRange;
	}

	public void setDataInsert(String dataInsert) {
		this.dataInsert = dataInsert;
	}

	public void setDataUpdate(String dataUpdate) {
		this.dataUpdate = dataUpdate;
	}

	public void setDataProcessing(String dataProcessing) {
		this.dataProcessing = dataProcessing;
	}
}