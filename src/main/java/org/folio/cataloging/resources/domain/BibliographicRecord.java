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
 *  <p>
 *
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "group", "leader", "fixedFields", "variableFields" })
public class BibliographicRecord {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("group")
    private Integer group;

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

}
