package org.folio.rest.jaxrs.model;

import javax.annotation.Generated;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Field
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "fixed-field", "variable-field" })
public class Field {

    /**
     * fixedField
     * <p>
     * 
     * 
     */
    @JsonProperty("fixed-field")
    @Valid
    private FixedField fixedField;

    /**
     * variableField
     * <p>
     * 
     * 
     */
    @JsonProperty("variable-field")
    @Valid
    private VariableField variableField;

    /**
     * fixedField
     * <p>
     * 
     * 
     * @return
     *     The fixedField
     */
    @JsonProperty("fixed-field")
    public FixedField getFixedField() {
        return fixedField;
    }

    /**
     * fixedField
     * <p>
     * 
     * 
     * @param fixedField
     *     The fixed-field
     */
    @JsonProperty("fixed-field")
    public void setFixedField(FixedField fixedField) {
        this.fixedField = fixedField;
    }

    public Field withFixedField(FixedField fixedField) {
        this.fixedField = fixedField;
        return this;
    }

    /**
     * variableField
     * <p>
     * 
     * 
     * @return
     *     The variableField
     */
    @JsonProperty("variable-field")
    public VariableField getVariableField() {
        return variableField;
    }

    /**
     * variableField
     * <p>
     * 
     * 
     * @param variableField
     *     The variable-field
     */
    @JsonProperty("variable-field")
    public void setVariableField(VariableField variableField) {
        this.variableField = variableField;
    }

    public Field withVariableField(VariableField variableField) {
        this.variableField = variableField;
        return this;
    }
}
