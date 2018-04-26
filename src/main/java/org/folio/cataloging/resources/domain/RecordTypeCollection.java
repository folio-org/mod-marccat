package org.folio.cataloging.resources.domain;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "recordTypes" })
public class RecordTypeCollection {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("recordTypes")
    @Valid
    @NotNull
    private List<RecordType> recordTypes = new ArrayList<RecordType>();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     * @return
     *     The recordTypes
     */
    @JsonProperty("recordTypes")
    public List<RecordType> getRecordTypes() {
        return recordTypes;
    }

    /**
     * 
     * (Required)
     * 
     * @param recordTypes
     *     The recordTypes
     */
    @JsonProperty("recordTypes")
    public void setRecordTypes(List<RecordType> recordTypes) {
        this.recordTypes = recordTypes;
    }

    public RecordTypeCollection withRecordTypes(List<RecordType> recordTypes) {
        this.recordTypes = recordTypes;
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

    public RecordTypeCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }
}
