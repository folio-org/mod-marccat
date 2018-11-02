package org.folio.marccat.business;

import org.folio.marccat.dao.persistence.CLCTN_CST_RULE;

public class RuleCSTListElement {
  CLCTN_CST_RULE rule;
  private Integer ruleId;
  private String ruleDescription;
  private String level;
  private String dataType;
  private String dataRange;
  private String dataUploadFrom;
  private String dataUploadTo;
  private String dataEmbRange;
  private String dataEmbFrom;
  private String dataEmbTo;
  private String dataInsert;
  private String dataUpdate;
  private String dataProcessing;
  private String flagProcessing;

  private String customerCode;
  private Long collectionSource;
  private Long collectionTarget;

  public RuleCSTListElement(CLCTN_CST_RULE rule) {
    setRule(rule);
    setRuleId(rule.getRuleId());
    setFlagProcessing(rule.getFlagProcessing());
    setRuleDescription(rule.getRuleDescription());
    setDataType(rule.getDataType());
  }

  public CLCTN_CST_RULE getRule() {
    return rule;
  }

  public void setRule(CLCTN_CST_RULE rule) {
    this.rule = rule;
  }

  public Integer getRuleId() {
    return ruleId;
  }

  public void setRuleId(Integer ruleId) {
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

  public String getDataEmbRange() {
    return dataEmbRange;
  }

  public void setDataEmbRange(String dataEmbRange) {
    this.dataEmbRange = dataEmbRange;
  }

  public String getDataEmbFrom() {
    return dataEmbFrom;
  }

  public void setDataEmbFrom(String dataEmbFrom) {
    this.dataEmbFrom = dataEmbFrom;
  }

  public String getDataEmbTo() {
    return dataEmbTo;
  }

  public void setDataEmbTo(String dataEmbTo) {
    this.dataEmbTo = dataEmbTo;
  }

  public String getCustomerCode() {
    return customerCode;
  }

  public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
  }

  public Long getCollectionSource() {
    return collectionSource;
  }

  public void setCollectionSource(Long collectionSource) {
    this.collectionSource = collectionSource;
  }

  public Long getCollectionTarget() {
    return collectionTarget;
  }

  public void setCollectionTarget(Long collectionTarget) {
    this.collectionTarget = collectionTarget;
  }
}
