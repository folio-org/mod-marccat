package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * BibliograpgicRecord
 * <p>
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"id", "group", "canadianContentIndicator", "verificationLevel", "leader", "fields", "recordView"})
public class BibliographicRecord {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("group")
  private Integer group;

  @JsonProperty("leader")
  @Valid
  private Leader leader;

  @JsonProperty("canadianContentIndicator")
  private String canadianContentIndicator;

  @JsonProperty("verificationLevel")
  private String verificationLevel;

  @JsonProperty("fields")
  @Valid
  private List <Field> fields = new ArrayList <Field>();

  @JsonProperty("recordView")
  private int recordView;


  /**
   * @return The id
   */
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  /**
   * @param id The id
   */
  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return The group
   */
  @JsonProperty("group")
  public Integer getGroup() {
    return group;
  }

  /**
   * @param group The group
   */
  @JsonProperty("group")
  public void setGroup(Integer group) {
    this.group = group;
  }


  /**
   * @return The leader
   */
  @JsonProperty("leader")
  public Leader getLeader() {
    return leader;
  }

  /**
   * @param leader The leader
   */
  @JsonProperty("leader")
  public void setLeader(Leader leader) {
    this.leader = leader;
  }

  /**
   * @return The fields
   */
  @JsonProperty("fields")
  public List <Field> getFields() {
    return fields;
  }

  /**
   * @param fields The fields
   */
  @JsonProperty("fields")
  public void setFields(List <Field> fields) {
    this.fields = fields;
  }

  /**
   * @return canadianContentIndicator.
   */
  @JsonProperty("canadianContentIndicator")
  public String getCanadianContentIndicator() {
    return canadianContentIndicator;
  }

  /**
   * @param canadianContentIndicator. the canadianContentIndicator
   */
  @JsonProperty("canadianContentIndicator")
  public void setCanadianContentIndicator(final String canadianContentIndicator) {
    this.canadianContentIndicator = canadianContentIndicator;
  }

  /**
   * @return verificationLevel.
   */
  @JsonProperty("verificationLevel")
  public String getVerificationLevel() {
    return verificationLevel;
  }

  /**
   * @param verificationLevel. the verificationLevel
   */
  @JsonProperty("verificationLevel")
  public void setVerificationLevel(final String verificationLevel) {
    this.verificationLevel = verificationLevel;
  }

  /**
   * @return the record view.
   */
  @JsonProperty("recordView")
  public int getRecordView() {
    return recordView;
  }

  /**
   * @param recordView
   */
  @JsonProperty("recordView")
  public void setRecordView(int recordView) {
    this.recordView = recordView;
  }
}
