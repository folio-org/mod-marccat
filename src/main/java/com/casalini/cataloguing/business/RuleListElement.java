package com.casalini.cataloguing.business;

import librisuite.hibernate.CLCTN_MST_RULE;

public class RuleListElement 
{
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
	
	public RuleListElement(CLCTN_MST_RULE rule) {
		setRule(rule);
		setRuleId(rule.getRuleId().intValue());
		setFlagProcessing(rule.getFlagProcessing());		
		setRuleDescription(rule.getRuleDescription());
		setDataType(rule.getDataType());
	}

	public CLCTN_MST_RULE getRule() {
		return rule;
	}
	
	public String getCollSource() {
		return collSource;
	}

	public void setCollSource(String collSource) {
		this.collSource = collSource;
	}

	public void setRule(CLCTN_MST_RULE rule) {
		this.rule = rule;
	}

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getDataRange() {
		return dataRange;
	}
	
	public void setDataRange(String dataRange) {
		this.dataRange = dataRange;
	}
	
	public String getDataInsert() {
		return dataInsert;
	}
	
	public void setDataInsert(String dataInsert) {
		this.dataInsert = dataInsert;
	}
	
	public String getDataUpdate() {
		return dataUpdate;
	}
	
	public void setDataUpdate(String dataUpdate) {
		this.dataUpdate = dataUpdate;
	}
	
	public String getDataProcessing() {
		return dataProcessing;
	}
	
	public void setDataProcessing(String dataProcessing) {
		this.dataProcessing = dataProcessing;
	}
	
	public String getFlagProcessing() {
		return flagProcessing;
	}
	
	public void setFlagProcessing(String flagProcessing) {
		this.flagProcessing = flagProcessing;
	}

	public String getDataUploadFrom() {
		return dataUploadFrom;
	}

	public void setDataUploadFrom(String dataUploadFrom) {
		this.dataUploadFrom = dataUploadFrom;
	}

	public String getDataUploadTo() {
		return dataUploadTo;
	}

	public void setDataUploadTo(String dataUploadTo) {
		this.dataUploadTo = dataUploadTo;
	}
}