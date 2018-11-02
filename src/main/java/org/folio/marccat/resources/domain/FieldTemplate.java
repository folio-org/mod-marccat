package org.folio.marccat.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;

/**
 * FieldTemplate
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"fixedField", "variable-field"})
public class FieldTemplate {

  /**
   * fixedField
   * <p>
   */
  @JsonProperty("fixed-field")
  @Valid
  private FixedField fixedField;

  /**
   * variableField
   * <p>
   */
  @JsonProperty("variable-field")
  @Valid
  private VariableField variableField;

  /**
   * fixedField
   * <p>
   *
   * @return The fixedField
   */
  @JsonProperty("fixed-field")
  public FixedField getFixedField() {
    return fixedField;
  }

  /**
   * fixedField
   * <p>
   *
   * @param fixedField The fixed-field
   */
  @JsonProperty("fixed-field")
  public void setFixedField(FixedField fixedField) {
    this.fixedField = fixedField;
  }

  public FieldTemplate withFixedField(FixedField fixedField) {
    this.fixedField = fixedField;
    return this;
  }

  /**
   * variableField
   * <p>
   *
   * @return The variableField
   */
  @JsonProperty("variable-field")
  public VariableField getVariableField() {
    return variableField;
  }

  /**
   * variableField
   * <p>
   *
   * @param variableField The variable-field
   */
  @JsonProperty("variable-field")
  public void setVariableField(VariableField variableField) {
    this.variableField = variableField;
  }

  public FieldTemplate withVariableField(VariableField variableField) {
    this.variableField = variableField;
    return this;
  }
}
