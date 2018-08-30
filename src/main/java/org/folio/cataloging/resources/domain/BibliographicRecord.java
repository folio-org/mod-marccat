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
@JsonPropertyOrder({ "id", "group", "leader", "fields" })
public class BibliographicRecord {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("group")
    private Integer group;

    @JsonProperty("leader")
    @Valid
    private Leader leader;

    @JsonProperty("fields")
    @Valid
    private List<Field> fields = new ArrayList<Field>();


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
     *     The fields
     */
    @JsonProperty("fields")
    public List<Field> getFields() {
        return fields;
    }

    /**
     *
     * @param fields
     *     The fields
     */
    @JsonProperty("fields")
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
