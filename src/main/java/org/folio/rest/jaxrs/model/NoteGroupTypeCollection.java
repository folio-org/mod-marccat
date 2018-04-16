package org.folio.rest.jaxrs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "noteGroupTypes" })
public class NoteGroupTypeCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("noteGroupTypes")
    @Valid
    @NotNull
    private List<NoteGroupType> noteGroupTypes = new ArrayList<NoteGroupType>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The noteGroupTypes
     */
    @JsonProperty("noteGroupTypes")
    public List<NoteGroupType> getNoteGroupTypes() {
        return noteGroupTypes;
    }

    /**
     * 
     * (Required)
     * 
     * @param noteGroupTypes
     *     The noteGroupTypes
     */
    @JsonProperty("noteGroupTypes")
    public void setNoteGroupTypes(List<NoteGroupType> noteGroupTypes) {
        this.noteGroupTypes = noteGroupTypes;
    }

    public NoteGroupTypeCollection withNoteGroupTypes(List<NoteGroupType> noteGroupTypes) {
        this.noteGroupTypes = noteGroupTypes;
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

    public NoteGroupTypeCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
