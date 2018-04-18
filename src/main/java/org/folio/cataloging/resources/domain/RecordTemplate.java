package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * RecordTemplate
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "name", "group", "type", "leader", "fixedFields", "variableFields" })
public class RecordTemplate {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("group")
    private Integer group;

    @JsonProperty("type")
    private String type;

    @JsonProperty("leader")
    @Valid
    private Leader leader;

    @JsonProperty("fixedFields")
    @Valid
    private List<FixedField> fixedFields = new ArrayList<FixedField>();

    @JsonProperty("variableFields")
    @Valid
    private List<VariableField> variableFields = new ArrayList<VariableField>();

    /**
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    public RecordTemplate withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public RecordTemplate withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The group
     */
    @JsonProperty("group")
    public Integer getGroup() {
        return group;
    }

    /**
     * 
     * @param group
     *     The group
     */
    @JsonProperty("group")
    public void setGroup(Integer group) {
        this.group = group;
    }

    public RecordTemplate withGroup(Integer group) {
        this.group = group;
        return this;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public RecordTemplate withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 
     * @return
     *     The leader
     */
    @JsonProperty("leader")
    public Leader getLeader() {
        return leader;
    }

    /**
     * 
     * @param leader
     *     The leader
     */
    @JsonProperty("leader")
    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    public RecordTemplate withLeader(Leader leader) {
        this.leader = leader;
        return this;
    }

    /**
     * 
     * @return
     *     The fixedFields
     */
    @JsonProperty("fixedFields")
    public List<FixedField> getFixedFields() {
        return fixedFields;
    }

    /**
     * 
     * @param fixedFields
     *     The fixedFields
     */
    @JsonProperty("fixedFields")
    public void setFixedFields(List<FixedField> fixedFields) {
        this.fixedFields = fixedFields;
    }

    public RecordTemplate withFixedFields(List<FixedField> fixedFields) {
        this.fixedFields = fixedFields;
        return this;
    }

    /**
     * 
     * @return
     *     The variableFields
     */
    @JsonProperty("variableFields")
    public List<VariableField> getVariableFields() {
        return variableFields;
    }

    /**
     * 
     * @param variableFields
     *     The variableFields
     */
    @JsonProperty("variableFields")
    public void setVariableFields(List<VariableField> variableFields) {
        this.variableFields = variableFields;
    }

    public RecordTemplate withVariableFields(List<VariableField> variableFields) {
        this.variableFields = variableFields;
        return this;
    }
}
