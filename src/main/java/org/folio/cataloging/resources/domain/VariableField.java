package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * variableField
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "keyNumber", "categoryCode", "headerTypeCode", "itemTypeCode", "functionCode", "ind1", "ind2", "code", "displayValue", "description", "mandatory", "subfields", "defaultSubfieldCode", "fieldStatus", "sequenceNumber" })
public class VariableField {

    @JsonProperty("keyNumber")
    private Integer keyNumber;

    @JsonProperty("categoryCode")
    private Integer categoryCode;

    @JsonProperty("headerTypeCode")
    private String headingTypeCode;

    @JsonProperty("itemTypeCode")
    private String itemTypeCode;

    @JsonProperty("functionCode")
    private String functionCode;

    @JsonProperty("ind1")
    @Size(min = 1, max = 1)
    private String ind1;

    @JsonProperty("ind2")
    @Size(min = 1, max = 1)
    private String ind2;

    @JsonProperty("code")
    private String code;

    @JsonProperty("displayValue")
    private String value;

    @JsonProperty("description")
    private String description;

    @JsonProperty("mandatory")
    private boolean mandatory;

    @JsonProperty("fieldStatus")
    private FieldStatus fieldStatus;

    @JsonProperty("sequenceNumber")
    private int sequenceNumber;

    @JsonProperty("subfields")
    @Valid
    private List<String> subfields = new ArrayList<String>();

    @JsonProperty("defaultSubfieldCode")
    @Pattern(regexp = "[a-z|0-9]")
    private String defaultSubfieldCode;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     *     The keyNumber
     */
    @JsonProperty("keyNumber")
    public Integer getKeyNumber() {
        return keyNumber;
    }

    /**
     *
     * @param keyNumber
     *     The keyNumber
     */
    @JsonProperty("keyNumber")
    public void setKeyNumber(Integer keyNumber) {
        this.keyNumber = keyNumber;
    }

    /**
     * 
     * @return
     *     The categoryCode
     */
    @JsonProperty("categoryCode")
    public Integer getCategoryCode() {
        return categoryCode;
    }

    /**
     * 
     * @param categoryCode
     *     The categoryCode
     */
    @JsonProperty("categoryCode")
    public void setCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
    }

    public VariableField withCategoryCode(Integer categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    /**
     * 
     * @return
     *     The headingTypeCode
     */
    @JsonProperty("headerTypeCode")
    public String getHeadingTypeCode() {
        return headingTypeCode;
    }

    /**
     * 
     * @param headingTypeCode
     *     The headingTypeCode
     */
    @JsonProperty("headerTypeCode")
    public void setHeadingTypeCode(String headingTypeCode) {
        this.headingTypeCode = headingTypeCode;
    }

    public VariableField withHeadingTypeCode(String headingTypeCode) {
        this.headingTypeCode = headingTypeCode;
        return this;
    }

    /**
     * 
     * @return
     *     The itemTypeCode
     */
    @JsonProperty("itemTypeCode")
    public String getItemTypeCode() {
        return itemTypeCode;
    }

    /**
     * 
     * @param itemTypeCode
     *     The itemTypeCode
     */
    @JsonProperty("itemTypeCode")
    public void setItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
    }

    public VariableField withItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
        return this;
    }

    /**
     * 
     * @return
     *     The functionCode
     */
    @JsonProperty("functionCode")
    public String getFunctionCode() {
        return functionCode;
    }

    /**
     * 
     * @param functionCode
     *     The functionCode
     */
    @JsonProperty("functionCode")
    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public VariableField withFunctionCode(String functionCode) {
        this.functionCode = functionCode;
        return this;
    }

    /**
     * 
     * @return
     *     The ind1
     */
    @JsonProperty("ind1")
    public String getInd1() {
        return ind1;
    }

    /**
     * 
     * @param ind1
     *     The ind1
     */
    @JsonProperty("ind1")
    public void setInd1(String ind1) {
        this.ind1 = ind1;
    }

    public VariableField withInd1(String ind1) {
        this.ind1 = ind1;
        return this;
    }

    /**
     * 
     * @return
     *     The ind2
     */
    @JsonProperty("ind2")
    public String getInd2() {
        return ind2;
    }

    /**
     * 
     * @param ind2
     *     The ind2
     */
    @JsonProperty("ind2")
    public void setInd2(String ind2) {
        this.ind2 = ind2;
    }

    public VariableField withInd2(String ind2) {
        this.ind2 = ind2;
        return this;
    }

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

    public VariableField withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * 
     * @return
     *     The value
     */
    @JsonProperty("displayValue")
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    @JsonProperty("displayValue")
    public void setValue(String value) {
        this.value = value;
    }

    public VariableField withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public VariableField withDescription(String description) {
        this.description = description;
        return this;
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
     *     The subfields
     */
    @JsonProperty("subfields")
    public List<String> getSubfields() {
        return subfields;
    }

    /**
     * 
     * @param subfields
     *     The subfields
     */
    @JsonProperty("subfields")
    public void setSubfields(List<String> subfields) {
        this.subfields = subfields;
    }

    public VariableField withSubfields(List<String> subfields) {
        this.subfields = subfields;
        return this;
    }

    /**
     * 
     * @return
     *     The defaultSubfieldCode
     */
    @JsonProperty("defaultSubfieldCode")
    public String getDefaultSubfieldCode() {
        return defaultSubfieldCode;
    }

    /**
     * 
     * @param defaultSubfieldCode
     *     The defaultSubfieldCode
     */
    @JsonProperty("defaultSubfieldCode")
    public void setDefaultSubfieldCode(String defaultSubfieldCode) {
        this.defaultSubfieldCode = defaultSubfieldCode;
    }

    public VariableField withDefaultSubfieldCode(String defaultSubfieldCode) {
        this.defaultSubfieldCode = defaultSubfieldCode;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public VariableField withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
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
     *
     * @return
     *     The sequenceNumber
     */
    @JsonProperty("sequenceNumber")
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     *
     * @param sequenceNumber
     *     The sequenceNumber
     */
    @JsonProperty("sequenceNumber")
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
