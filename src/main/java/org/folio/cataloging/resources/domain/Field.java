package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Field
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "code", "mandatory", "fieldStatus", "fixedField", "variableField" })
public class Field {

    @JsonProperty("code")
    private String code;

    @JsonProperty("mandatory")
    private boolean mandatory;

    @JsonProperty("fieldStatus")
    private FieldStatus fieldStatus;


    /**
     *
     * @return
     *     The code
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     *     The code
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }



    /**
     *
     * @return
     *      return if mandatory field.
     */
    @JsonProperty("mandatory")
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     *
     * @param mandatory
     *      the mandatory flag to set.
     */
    @JsonProperty("mandatory")
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     *
     * @return
     *     the field status.
     */
    @JsonProperty("fieldStatus")
    public FieldStatus getFieldStatus() {
        return fieldStatus;
    }

    /**
     *
     * @param fieldStatus
     *      the field status to set.
     */
    @JsonProperty("fieldStatus")
    public void setFieldStatus(FieldStatus fieldStatus) {
        this.fieldStatus = fieldStatus;
    }

    /**
     * fixedField
     * <p>
     * 
     * 
     */
    @JsonProperty("fixedField")
    @Valid
    private FixedField fixedField;

    /**
     * variableField
     * <p>
     * 
     * 
     */
    @JsonProperty("variableField")
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
    @JsonProperty("fixedField")
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
    @JsonProperty("fixedField")
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
    @JsonProperty("variableField")
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
    @JsonProperty("variableField")
    public void setVariableField(VariableField variableField) {
        this.variableField = variableField;
    }

    public Field withVariableField(VariableField variableField) {
        this.variableField = variableField;
        return this;
    }

    @Generated("org.jsonschema2pojo")
    public static enum FieldStatus {
        NEW("new"), CHANGED("changed"), DELETED("deleted"), UPDATED("updated"), UNCHANGED("unchanged");

        private final String value;

        private static final Map<String, FieldStatus> CONSTANTS = new HashMap<String, FieldStatus>();

        static {
            for (FieldStatus c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private FieldStatus(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static FieldStatus fromValue(String value) {
            FieldStatus constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}
